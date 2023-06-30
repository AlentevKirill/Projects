package ru.itmo.web.hw4.util;

import ru.itmo.web.hw4.model.Post;
import ru.itmo.web.hw4.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataUtil {
    private static final List<User> USERS = Arrays.asList(
            new User(1, "MikeMirzayanov", "Mike Mirzayanov"),
            new User(6, "pashka", "Pavel Mavrin"),
            new User(9, "geranazavr555", "Georgiy Nazarov"),
            new User(11, "tourist", "Gennady Korotkevich")
    );

    private static final List<Post> POSTS = Arrays.asList(
            new Post(12321, "Codeforces Round #510 (Div. 2)", "Advertising companies say advertising is" +
                    " necessary and important. It informs people about new products. Advertising hoardings in the street" +
                    " make our environment colourful. And adverts on TV are often funny. Sometimes they are mini-dramas" +
                    " and we wait for the next programme in the mini-drama. Advertising can educate, too." +
                    " Adverts tell us about new, healthy products. And adverts in magazines give us ideas for how to" +
                    " look prettier, be fashionable and be successful. Without advertising life is boring and colourless.",
                    1),
            new Post(2353425, "Codeforces Round #510 (Div. 2)", "But some consumers argue that advertising" +
                    " is a bad thing. They say that advertising is bad for children. Adverts make children 'pester'" +
                    " their parents to buy things for them. Advertisers know we love our children and want to give" +
                    " them everything. So they use children's 'pester power' to sell their products. Finally," +
                    " consumers say, if there is advertising there must be rules. Some adverts advertise unhealthy" +
                    " things like cigarettes and make people waste their money.", 6),
            new Post(3245, "Codeforces Round #510 (Div. 2)", "Marco Polo is famous for his journeys across Asia." +
                    " He was one of the first Europeans to travel in Mongolia and China. He wrote a famous book called" +
                    " 'The Travels'.", 9),
            new Post(543636, "Codeforces Round #510 (Div. 2)", "He was born in Venice, Italy in 1254." +
                    " In 1272, when he was only 17 years old, he travelled to Asia with his father and uncle." +
                    " The journey was very long. They visited a lot of places and saw wonderful things:" +
                    " eye glasses, ice-cream, spaghetti and the riches of Asia.", 11)
    );

    public static void addData(HttpServletRequest request, Map<String, Object> data) {
        data.put("users", USERS);
        data.put("posts", POSTS);

        for (User user : USERS) {
            if (Long.toString(user.getId()).equals(request.getParameter("logged_user_id"))) {
                data.put("user", user);
            }
        }
    }
}
