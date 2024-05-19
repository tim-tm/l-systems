# L-systems

This is a "simple" L-system implementation in Java using [Jaylib](https://github.com/electronstudio/jaylib) ([Raylib](https://www.raylib.com/) bindings for Java).

By default, a hardcoded version of [this](https://github.com/tim-tm/l-systems/blob/main/app/l-systems/fractal_tree.lsys) L-system will be loaded.
You may want to specify a custom L-system via. the program args.

```sh
lsystems <file>
```

Loading a custom L-system requires you to save your system into a text file. Again, the default model may be a good example for the syntax of the so-called .lsys files.

```sh
Y               # axiom (the initial state of the L-system)
X:XX            # rule nr. 1, X --> XX
Y:X[-Y][+Y]     # rule nr. 2, Y --> X[-Y][+Y]
-:-45           # rotation in degrees
+:45
```

For now, you are locked into using line segments. Other meanings for uppercase characters, such as "draw forward" or "moveforward" will be implemented pretty soon.
