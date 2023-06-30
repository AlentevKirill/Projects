package javaAdvanced.info.kgeorgiy.ja.alentev.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of interfaces {@link Impler} and {@link JarImpler}.
 *
 * @author Kirill
 */
public class Implementor implements Impler, JarImpler {

    @Override
    public void implement(Class<?> aClass, Path path) throws ImplerException {
        checkInputArgs(aClass, path);
        if (Modifier.isPrivate(aClass.getModifiers())
                || Modifier.isFinal(aClass.getModifiers())
                || aClass.isPrimitive()
                || aClass == Enum.class
                || aClass.isArray()) {
            throw new ImplerException("Class cannot be inherited");
        }
        Path newPathName = getNewPathName(aClass, path);
        createDir(newPathName);
        try (BufferedWriter writer = Files.newBufferedWriter(newPathName /*Paths.get("C:\\Users\\Кирилл\\Desktop\\java-advanced\\test.txt")*/,
                StandardCharsets.UTF_8)) {
            // :NOTE: default package
            String packageName = aClass.getPackageName().equals("") ?
                    "" :
                    "package " + aClass.getPackageName() + ";";
            String signatureOfClass = "public class " + aClass.getSimpleName() + "Impl " +
                    (aClass.isInterface() ? "implements" : "extends") + " " +
                    aClass.getCanonicalName() + " {";
            writer.write(String.format("%s%n%s%n", packageName, signatureOfClass));
            writeConstructor(aClass, writer);
            writeMethods(aClass, writer);
            writer.write("}");
        } catch (IOException e) {
            throw new ImplerException("Error with writing file" + e.getMessage(), e);
        }
    }

    /**
     * Checks whether the arguments are null.
     *
     * @param aClass first argument to check
     * @param path second argument to check
     * @throws ImplerException If an argument with a null value was found
     */
    private void checkInputArgs(Class<?> aClass, Path path) throws ImplerException {
        if (Objects.isNull(aClass)) {
            throw new ImplerException("The passed class must not be null");
        }
        if (Objects.isNull(path)) {
            throw new ImplerException("The passed path must not be null");
        }
    }

    /**
     * Returns the path to the implementation of the class located in the directory path.
     *
     * @param aClass the {@link Class} from which inheritance occurs
     * @param path the path to the inherited class
     * @return path to class implementation.
     */
    private Path getNewPathName(Class<?> aClass, Path path) {
        String fileName = aClass.getPackageName();
        fileName = fileName.replace(".", File.separator) + File.separator + aClass.getSimpleName();
        return path.resolve(fileName + "Impl.java");
    }

    /**
     * Returns the relative path from the package root to the compiled implementation of the class.
     *
     * @param aClass the {@link Class} from which inheritance occurs
     * @return relative path to compiled class implementation.
     */
    private Path getClassPath(Class<?> aClass) {
        return Path.of(aClass.getPackageName().replace(".", File.separator) +
                File.separator + aClass.getSimpleName() + "Impl.class");
    }

    /**
     * If there is no directory on the specified path, then creates a new directory.
     *
     * @param path the path to the file to be checked
     * @throws ImplerException If it was not possible to create a directory for the file
     */
    private void createDir(Path path) throws ImplerException {
        if (!Files.isWritable(path)) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new ImplerException("Error with writing file" + e.getMessage(), e);
            }
        }
    }

    /**
     * Returns a string representation of the method/constructor header.
     * The abstract, native, and transient modifiers will be removed from the method signature.
     *
     * @param executable The method/constructor whose parameters will be used to build the header
     * @param returnParameter Return values of the constructor/method
     * @return A string representing the signature of a method/constructor.
     */
    private String getSignatureString(Executable executable, String returnParameter) {
        // :NOTE: executable with varargs
        //result = result.replaceAll("(abstract|native|transient)", "");
        return Modifier.toString(executable.getModifiers() &
                ~Modifier.NATIVE & ~Modifier.TRANSIENT & ~Modifier.ABSTRACT) + " " +
                returnParameter +
                Arrays.stream(executable.getParameters()).
                        map(parameter -> (parameter.getModifiers() != 0 ?
                                Modifier.toString(parameter.getModifiers()) + " " :
                                "") + parameter.getType().getCanonicalName() +
                                " " + parameter.getName()).
                        collect(Collectors.joining(", ", "(", ") ")) +
                (executable.getExceptionTypes().length == 0 ? "" : "throws " +
                        Arrays.stream(executable.getExceptionTypes()).
                                map(Class::getCanonicalName).
                                collect(Collectors.joining(", ")));
    }


    /**
     * Generates and writes to the passed writer the implementations of all non-private constructors of the passed class.
     * An example of one of the constructors
     * <blockquote><pre>
     * public Proxies(int arg0, int arg1) {
     * super(argo, arg1);
     * }
     *
     * </pre></blockquote>
     * If the passed class turned out to be an interface, then the method terminates without doing anything.
     *
     * @param aClass The class from which constructors are inherited
     * @param writer A writer for writing to a file
     * @throws IOException If it didn't work out to write to the writer
     * @throws ImplerException If there are no non-private constructors in the parent class
     */
    private void writeConstructor(Class<?> aClass, BufferedWriter writer) throws IOException, ImplerException {
        if (aClass.isInterface()) {
            return;
        }
        List<String> list = Arrays.stream(aClass.getDeclaredConstructors()).
                filter(constructor -> !Modifier.isPrivate(constructor.getModifiers())).
                map(constructor -> {
                    String sign = getSignatureString(constructor, aClass.getSimpleName() + "Impl");
                    String body = Arrays.stream(constructor.getParameters()).
                            map(Parameter::getName).
                            collect(Collectors.joining(", ", "super(", ");"));
                    return String.format("%s {%n%s%n}%n", sign, body);
                }).collect(Collectors.toCollection(ArrayList::new));
        if (list.isEmpty()) {
            throw new ImplerException("There is no non-public constructor");
        }
        for (String string :
                list) {
            writer.write(string);
        }
    }

    /**
     * Returns a set of non-abstract and non-final class methods found using the passed function.
     *
     * @param aClass The class whose methods are being considered
     * @param function Function for finding class methods
     * @return Filtered set of methods.
     */
    private Set<Method> getUniqMethods(Class<?> aClass, Function<Class<?>, Method[]> function) {
        return deleteRepeat(Arrays.stream(function.apply(aClass)).
                filter(method -> Modifier.isAbstract(method.getModifiers()) &&
                        !Modifier.isFinal(method.getModifiers()))).collect(Collectors.toSet());
    }

    // :NOTE: тут заменить на streams

    /**
     * Removes all the same methods from the stream using the method equals in {@link Wrapper}
     *
     * @param set stream of methods
     * @return stream of uniq methods.
     */
    private Stream<Method> deleteRepeat(Stream<Method> set) {
        return set.
                map(Wrapper::new).
                distinct().
                map(Wrapper::getMethod);
    }

    /**
     * Generates and writes to the passed writer the implementations of all uniq methods of the passed class and his parent.
     * The method body is the output of the default value.
     * An example of one of the methods
     * <blockquote><pre>
     * public boolean hello(int arg0) {
     * return false;
     * }
     *
     * </pre></blockquote>
     *
     * @param aClass The class from which methods are inherited
     * @param writer A writer for writing to a file
     * @throws IOException If it didn't work out to write to the writer
     */
    private void writeMethods(Class<?> aClass, BufferedWriter writer) throws IOException {
        Set<Method> set = new HashSet<>(getUniqMethods(aClass, Class::getMethods));
        for (Class<?> clazz = aClass; clazz != null; clazz = clazz.getSuperclass()) {
            set.addAll(getUniqMethods(aClass, Class::getDeclaredMethods));
        }
        ArrayList<String> list = deleteRepeat(set.stream()).
                map(method -> {
                    String sign = getSignatureString(method, method.getReturnType().getCanonicalName() +
                            " " + method.getName());
                    Class<?> returnParam = method.getReturnType();
                    String defaultParam = "";
                    if (returnParam.equals(boolean.class)) {
                        defaultParam = "false";
                    } else if (returnParam.equals(void.class)) {
                        defaultParam = "";
                    } else if (returnParam.isPrimitive()) {
                        defaultParam = "0";
                    } else {
                        defaultParam = "null";
                    }
                    return String.format("%s {%n return %s;%n}%n", sign, defaultParam);
                }).collect(Collectors.toCollection(ArrayList::new));
        for (String string :
                list) {
            writer.write(string);
        }
    }

    /**
     * Compiles the specified file in the specified location.
     *
     * @param root path to the place where the file is compiled
     * @param file relative file path
     */
    private static void compileFiles(final Path root, final Path file) {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final String classpath = root + File.pathSeparator + getClassPath();
        final String[] args = Stream.concat(Stream.of(file.toString()), Stream.of("-cp", classpath)).toArray(String[]::new);
        compiler.run(null, null, null, args);
    }

    /**
     * Return {@link JarImpler} classpath.
     *
     * @return string representation {@link JarImpler} classpath.
     */
    private static String getClassPath() {
        try {
            return Path.of(JarImpler.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
        } catch (final URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public void implementJar(Class<?> aClass, Path path) throws ImplerException {
        checkInputArgs(aClass, path);
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        Path temp;
        createDir(path);
        try {
            temp = Files.createTempDirectory(".");
        } catch (IOException e) {
            throw new ImplerException("Can't create temp dir", e);
        }
        implement(aClass, temp);
        compileFiles(temp, getNewPathName(aClass, path).toAbsolutePath());
        try (JarOutputStream out = new JarOutputStream(Files.newOutputStream(path), manifest)) {
            JarEntry jarEntry = new JarEntry(getClassPath(aClass).toString());
            out.putNextEntry(jarEntry);
            try (InputStream reader = Files.newInputStream(temp.resolve(getClassPath(aClass)).toAbsolutePath())) {
                byte[] buffer = new byte[512];
                int count;
                while ((count = reader.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.closeEntry();
            }

        } catch (IOException e) {
            throw new ImplerException("Error with create jar file", e);
        }
    }

    /**
     * A class overriding the equals method for {@link Method}
     */
    private static class Wrapper {

        /**
         * A method being stored inside a wrapper
         */
        private final Method method;

        /**
         * Constructor for creating {@link Wrapper} from {@link Method}
         *
         * @param method method to store
         */
        Wrapper(Method method) {
            this.method = method;
        }

        /**
         * Getter for field method
         *
         * @return the stored method.
         */
        public Method getMethod() {
            return method;
        }

        /**
         * Returns the result of the hash function of the {@link Objects} class applied to the return value, name, and array of method parameters.
         *
         * @return integer hash value.
         */
        @Override
        public int hashCode() {
            return Objects.hash(method.getReturnType(), method.getName(),
                    Arrays.hashCode(method.getParameterTypes()));
        }

        /**
         * The operation of comparing methods by string representation of their return values, names and parameters.
         *
         * @param obj the object with which the method is compared
         * @return true if the string values being compared match, otherwise false.
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Wrapper otherMethod = (Wrapper) obj;
            return (method.getName().equals(otherMethod.method.getName())) &&
                    Arrays.equals(method.getParameterTypes(), otherMethod.method.getParameterTypes()) &&
                    method.getReturnType().equals(otherMethod.method.getReturnType());

        }

    }

    /**
     * Main function of class {@link Implementor}.
     * If 2 arguments are received, it tries to create a class implementing a class named arg[0] and puts it on the path arg[1].
     * If 3 arguments are received, it tries to create a jar archive with the implementation of the class arg[1] and sends it along the path arg[2].
     *
     * @param args arguments of command line
     * @see #implement(Class, Path)
     * @see #implementJar(Class, Path)
     */
    public static void main(String[] args) {
        if (args == null) {
            System.err.println("Arguments must not be null");
            return;
        }
        if (args.length != 2 && args.length != 3) {
            System.err.println("There should be 2 or 3 arguments");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                System.err.println("Argument " + i + " must not be null");
                return;
            }
        }
        try {
            if (args.length == 2) {
                new Implementor().implement(Class.forName(args[0]), Paths.get(args[1]));
            } else if (args[0].equals("-jar")) {
                new Implementor().implement(Class.forName(args[1]), Paths.get(args[2]));
            } else {
                System.err.println("The first argument of the three should be -jar");
            }
        } catch (ImplerException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Invalid Class name: " + e);
        } catch (InvalidPathException e) {
            System.err.println("Invalid path: " + e);
        }
        /*try {
            new Implementor().implement(AccessibleAction.class, Paths.get(""));
        } catch (ImplerException e) {
            throw new RuntimeException(e);
        }*/
    }
}
