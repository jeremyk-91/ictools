# ictools

![CircleCI Build Status]
(https://circleci.com/gh/jeremyk-91/ictools.svg?&style=shield&circle-token=12a506937730d8214c6a96d7893a2cfea7874de6)

*ictools* is a series of utilities that I've written to help verify the correctness (or incorrectness) of the results of applying simple algorithms. The tool is motivated by a frustration I had during my time at Imperial College London with past exam papers; no solutions are available for these and it can be difficult to determine whether one's answers are correct. I've done this in the past by offering my solutions for public consumption (in practice, errors would likely be quickly pointed out - this is a similar rationale as to why cryptographic algorithms should be public), but a response isn't always forthcoming especially early on in the term.

# Modules Supported

- C220 Software Engineering Algorithms
  - Graph Algorithms
    - Fundamentals
      - Breadth-First Search (plus a few simple variants)
    - Maximum Flow
      - Ford-Fulkerson (implemented as Edmonds-Karp)
      - Dinic's algorithm (blocking flows; not in syllabus)
      - Push-Relabel (not in syllabus)
