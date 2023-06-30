prime(1) :- !.
prime(2) :- !.
prime(N) :- 0 is mod(N, 2), !, \+ true.
check(N, R) :- 0 is mod(N, R), !.
check(N, R) :- (R * R) < (N + 1), R1 = R + 1, check(N, R1).
prime(N) :- \+ check(N, 2).
composite(N) :- N > 1, \+ prime(N).
up([]) :- !.
up([H | []]) :- !.
up([H, HH | T]) :- HH >= H, up([HH | T]).
con([], R, N) :- R is N, !.
con([H | T], R, N) :- R1 is R * H, con(T, R1, N).
clean([], R, N) :- !, up(R), con(R, 1, N).
clean([H | T], R, N) :- composite(H), !, clean(T, R, N).
clean([H | T], R, N) :- \+ (0 is mod(N, H)), !, clean(T, R, N).
clean([H | T], R, N) :- append(R, [H], R1), clean(T, R1, N).
create([], Real, 1, I, Ic) :- !.
create(List, Real, N, I, Ic) :- \+ (0 is mod(N, Real)), !, Real1 is Real + 1, create(List, Real1, N, Ic, Ic).
create([H | T], Real, N, 1, Ic) :- H is Real, !, N1 is div(N, Real), create(T, Real, N1, Ic, Ic).
create([H | T], Real, N, I, Ic) :- H is Real, I1 is I - 1, create(T, Real, N, I1, Ic).
prime_divisors(N, Divisors) :- \+ (is_list(Divisors)), !, create(Divisors, 2, N, 1, 1).
prime_divisors(N, Divisors) :- clean(Divisors, [], N).
power_divisors(N, 0, []) :- !.
power_divisors(N, I, D) :- create(D, 2, N, I, I).