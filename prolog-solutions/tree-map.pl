knot(K, V, Left, Right, knot(K, V, Left, Right)).
knot(K, V, knot(K, V)).
knot(K, V, Right, knot(K, V, Right)).
nth0(0, [Elem|_Tail], Elem):-!.
nth0(Index, _List, _Elem):-
   Index < 0, !, fail.
nth0(Index, [_Head|Tail], Elem):-
   NextIndex is Index - 1,
   nth0(NextIndex, Tail, Elem).
create([(K, V) | []], Tree, R) :- !, R = knot(K, V), H1 = R, Tree = [R].
create([(K1, V1), H2| []], Tree, R) :- !, create([H2 | []], Tree1, R1),
	R = knot(K1, V1, R1),
	append([R], Tree1, Tree).
create(List, Tree, R) :- length(List, Length), Len is Length + 1, Mid is div(Len, 2) - 1,
	nth0(Mid, List, (K, V)), append(ListL, [(K, V) | ListR], List),
	create(ListL, Tree1, Rl),
	create(ListR, Tree2, Rr),
	R = knot(K, V, Rl, Rr), append([R | Tree1], Tree2, Tree).
map_build([], []).
map_build(ListMap, TreeMap) :- create(ListMap, TreeMap, R).
search(knot(K, V), Key, Value) :- K = Key, V = Value.
search(knot(K, V, Right), Key, Value) :- K = Key, V = Value, !.
search(knot(K, V, Right), Key, Value) :- search(Right, Key, Value).
search(knot(K, V, Left, Right), Key, Value) :- K = Key, V = Value, !.
search(knot(K, V, Left, Right), Key, Value) :- Key > K, !, search(Right, Key, Value).
search(knot(K, V, Left, Right), Key, Value) :- Key < K, search(Left, Key, Value).
map_get([H | T], Key, Value) :- search(H, Key, Value).
keys(knot(K, V), Keys) :- append([], [K], Keys).
keys(knot(K, V, Right), Keys) :- keys(Right, Keys1), append([K], Keys1, Keys).
keys(knot(K, V, Left, Right), Keys) :- keys(Left, Keys1), keys(Right, Keys2), append(Keys1, [K | Keys2], Keys).
map_keys([], []).
map_keys([H | T], Keys) :- keys(H, Keys).
values(knot(K, V), Values) :- append([], [V], Values).
values(knot(K, V, Right), Values) :- values(Right, Values1), append([V], Values1, Values).
values(knot(K, V, Left, Right), Values) :- values(Left, Values1), values(Right, Values2), append(Values1, [V | Values2], Values).
map_values([], []).
map_values([H | T], Values) :- values(H, Values).

	