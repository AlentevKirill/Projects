import * as stream from "stream";

export const NOMINATIVE : 1 = 1; // именительный
export const GENITIVE : 2 = 2; // родительный
export const DATIVE : 3 = 3; // дательный
export const ACCUSATIVE : 4 = 4; // винительный
export const INSTRUMENTAL : 5 = 5; // творительный
export const PREPOSITIONAL : 6 = 6; // предложный

export const MALE : 1 = 1;
export const FEMALE : 2 = 2;
export const ANDROGYNOUS : 4 = 4;

export function endsWith(str : string, search : string) : boolean {
  const strLength : number = str.length;
  return str.substring(strLength - search.length, strLength) === search;
}

export function startsWith(str : string, search : string, pos? : number) : boolean {
  return str.substr(!pos || pos < 0 ? 0 : +pos, search.length) === search;
}

export function getGenderConst(key: string | Gender | null | undefined) : Gender | null {
  switch (key) {
    case 'male':
    case MALE:
      return MALE;
    case 'female':
    case FEMALE:
      return FEMALE;
    case 'androgynous':
    case ANDROGYNOUS:
      return ANDROGYNOUS;
    default:
      return null;
  }
}

type RuleMale = {
  [key: string]: string[];
};


type RuleSet2 = {
  exceptions: RuleMale;
  suffixes: RuleMale;
};

export function getGenderByRuleSet(name : string, ruleSet : RuleSet2) : Gender | null {
  if (!name || !ruleSet) {
    return null;
  }
  const nameLower : string = name.toLowerCase();
  if (ruleSet.exceptions) {
    const gender : Gender | null = getGenderByRule(ruleSet.exceptions, (some : string) : boolean => {
      if (startsWith(some, '-')) {
        return endsWith(nameLower, some.substr(1));
      }
      return some === nameLower;
    });
    if (gender) return gender;
  }
  return ruleSet.suffixes
    ? getGenderByRule(ruleSet.suffixes, (some : string) : boolean => endsWith(nameLower, some))
    : null;
}

export function getGenderByRule(rules : RuleMale, matchFn : (s : string) => boolean) : Gender | null {
  const genders : string[] = Object.keys(rules).filter((genderKey : string) : boolean => {
    const array : string[] = rules[genderKey]!;
    return Array.isArray(array) && array.some(matchFn);
  });
  if (genders.length !== 1) {
    return null;
  }

  return getGenderConst(genders[0]);
}

export function inclineByRules(str :string, declensionStr : AllNumberConst, genderStr : Gender, ruleSet : RuleSet1) : string {
  const declension : AllNumberConst | null = getDeclensionConst(declensionStr);
  const gender : Gender | null = getGenderConst(genderStr);

  const parts : string[] = str.split('-');
  const result : string[] = [];

  for (let i : number = 0; i < parts.length; i++) {
    const part : string = parts[i]!;
    const isFirstWord : boolean = i === 0 && parts.length > 1;

    const rule : Rule | null = findRule(part, gender!, ruleSet, {
      firstWord: isFirstWord,
    });

    if (rule) {
      result.push(applyRule(rule, part, declension!));
    } else {
      result.push(part);
    }
  }
  return result.join('-');
}

type Gender = 1 | 2 | 4;

type RuleSet1 = {
  exceptions: Rule[];
  suffixes: Rule[];
}

export function findRule(str : string, gender : Gender, ruleSet :RuleSet1, tags : {[key: string]: boolean} = {}) : Rule | null {
  if (!str) {
    return null;
  }
  const strLower : string = str.toLowerCase();

  const tagList : string[] = [];
  Object.keys(tags).forEach((key : string) : void => {
    if (tags[key]) {
      tagList.push(key);
    }
  });

  if (ruleSet.exceptions) {
    const rule : Rule | null = findExactRule(ruleSet.exceptions, gender, (some : string | undefined) : boolean => some === strLower, tagList);
    if (rule) return rule;
  }

  return ruleSet.suffixes
    ? findExactRule(ruleSet.suffixes, gender, (some : string | undefined) : boolean => endsWith(strLower, some!), tagList)
    : null;
}

type BoolFunc = (s? : string) => boolean;

export function findExactRule(rules: Rule[], gender : Gender, matchFn : BoolFunc, tags : string[] = []) : Rule | null {
  for (let i : number = 0; i < rules.length; i++) {
    const rule : Rule = rules[i]!;

    if (rule.tags) {
      if (!rule.tags.find((t : string) : boolean =>  tags.indexOf(t) !== -1)) {
        continue;
      }
    }

    if (rule.gender !== ANDROGYNOUS && gender !== rule.gender) {
      continue;
    }

    if (rule.test) {
      for (let j : number = 0; j < rule.test.length; j++) {
        if (matchFn(rule.test[j])) {
          return rule;
        }
      }
    }
  }
  return null;
}

export function getModByIdx(mods : string[], i : number) : string | undefined {
  if (mods && mods.length >= i) {
    return mods[i];
  }
  return '.';
}

type Rule = {
  gender?: Gender;
  mods: string[];
  test?: string[];
  tags?: string[];
}

export function applyRule(rule : Rule, str : string, declension : AllNumberConst) : string {
  let mod : string;
  switch (declension) {
    case NOMINATIVE:
      mod = '.';
      break;
    case GENITIVE:
      mod = getModByIdx(rule.mods, 0)!;
      break;
    case DATIVE:
      mod = getModByIdx(rule.mods, 1)!;
      break;
    case ACCUSATIVE:
      mod = getModByIdx(rule.mods, 2)!;
      break;
    case INSTRUMENTAL:
      mod = getModByIdx(rule.mods, 3)!;
      break;
    case PREPOSITIONAL:
      mod = getModByIdx(rule.mods, 4)!;
      break;
    default:
      mod = '.';
  }

  return applyMod(str, mod);
}

export function applyMod(str : string, mod : string) : string {
  for (let i : number = 0; i < mod.length; i++) {
    const chr : string = mod[i]!;
    switch (chr) {
      case '.':
        break;
      case '-':
        str = str.substr(0, str.length - 1);
        break;
      default:
        str += chr;
    }
  }
  return str;
}

type AllNumberConst = 1 | 2 | 3 | 4 | 5 | 6;
type AllWordConst = 'nominative' | 'genitive' | 'dative' | 'accusative' | 'instrumental' | 'prepositional';


export function getDeclensionConst(key : string | AllNumberConst | null) : AllNumberConst | null {
  switch (key) {
    case 'nominative':
    case NOMINATIVE:
      return NOMINATIVE;
    case 'genitive':
    case GENITIVE:
      return GENITIVE;
    case 'dative':
    case DATIVE:
      return DATIVE;
    case 'accusative':
    case ACCUSATIVE:
      return ACCUSATIVE;
    case 'instrumental':
    case INSTRUMENTAL:
      return INSTRUMENTAL;
    case 'prepositional':
    case PREPOSITIONAL:
      return PREPOSITIONAL;
    default:
      return null;
  }
}

export function getDeclensionStr(cnst : string | AllNumberConst | null) : AllWordConst | null {
  switch (cnst) {
    case 'nominative':
    case NOMINATIVE:
      return 'nominative';
    case 'genitive':
    case GENITIVE:
      return 'genitive';
    case 'dative':
    case DATIVE:
      return 'dative';
    case 'accusative':
    case ACCUSATIVE:
      return 'accusative';
    case 'instrumental':
    case INSTRUMENTAL:
      return 'instrumental';
    case 'prepositional':
    case PREPOSITIONAL:
      return 'prepositional';
    default:
      return null;
  }
}
