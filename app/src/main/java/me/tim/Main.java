package me.tim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static com.raylib.Jaylib.RAYWHITE;
import static com.raylib.Jaylib.BLACK;
import static com.raylib.Jaylib.LIGHTGRAY;
import static com.raylib.Raylib.*;

public class Main {
    public static final int screenWidth = 800, screenHeight = 600;

    private ArrayList<Rule> rules;
    private LSystem system;

    private final int line_length;
    private final Camera2D camera;
    private float plus_angle;
    private float minus_angle;

    public Main() {
        this.rules = new ArrayList<>();
        this.rules.add(new Rule('X', "XX"));
        this.rules.add(new Rule('Y', "X[-Y][+Y]"));
        this.system = new LSystem(this.rules, "Y");
        this.plus_angle = 45.f;
        this.minus_angle = -45.f;

        this.line_length = 25;
        this.camera = new Camera2D();
        this.camera.zoom(1.f);
    }

    public Optional<String> parse(String filename) {
        if (filename.isEmpty() || filename.isBlank()) return Optional.ofNullable("Filename is empty.");

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            this.rules.clear();
            String start = "";
            String line = reader.readLine();
            while (line != null && !line.isEmpty() && !line.isBlank()) {
                String[] splt = line.split(":");
                if (splt == null || splt.length < 2) {
                    start = line;
                } else if (splt[0].equals("-")) {
                    this.minus_angle = Float.parseFloat(splt[1]);
                } else if (splt[0].equals("+")) {
                    this.plus_angle = Float.parseFloat(splt[1]);
                } else {
                    this.rules.add(new Rule(splt[0].charAt(0), splt[1]));
                }
                line = reader.readLine();
            }
            this.system = new LSystem(this.rules, start);
            System.out.println("Loaded L-system: \n" + this.system.toString());
        } catch (IOException exception) {
            return Optional.ofNullable("Failed to read \"" + filename + "\": " + exception.getMessage());
        }
        return Optional.ofNullable(null);
    }

    public void update() {
        if (IsMouseButtonDown(MOUSE_BUTTON_LEFT)) {
            Vector2 delta = GetMouseDelta();
            delta = Vector2Scale(delta, -1.0f/camera.zoom());
            camera.target(Vector2Add(camera.target(), delta));
        }
        if (IsKeyPressed(KEY_RIGHT)) {
            this.getSystem().step();
        }

        float wheel = GetMouseWheelMove();
        if (wheel != 0) {
            Vector2 mouseWorldPos = GetScreenToWorld2D(GetMousePosition(), camera);
            camera.offset(GetMousePosition());
            camera.target(mouseWorldPos);

            final float zoomIncrement = 0.125f;
            camera.zoom(camera.zoom()+(wheel*zoomIncrement));
            if (camera.zoom() < zoomIncrement) camera.zoom(zoomIncrement);
        }
    }

    public void draw() {
        BeginDrawing();
        ClearBackground(RAYWHITE);

        BeginMode2D(this.camera);
        rlPushMatrix();
        rlTranslatef(screenWidth/3, screenHeight, 0);
        for (char c : this.getSystem().getCurrent().toCharArray()) {
            switch (c) {
                case 'X':
                case 'Y': {
                    DrawLine(0, 0, 0, -this.line_length, BLACK);
                    rlTranslatef(0, -this.line_length, 0);
                    break;
                }
                case '-': {
                    rlRotatef(this.minus_angle, 0.f, 0.f, -1.f);
                    break;
                }
                case '+': {
                    rlRotatef(this.plus_angle, 0.f, 0.f, -1.f);
                    break;
                }
                case '[': {
                    rlPushMatrix();
                    break;
                }
                case ']': {
                    rlPopMatrix();
                    break;
                }
            }
        }
        rlPopMatrix();
        EndMode2D();

        DrawRectangle(screenWidth-screenWidth/3, 0, screenWidth, screenHeight, LIGHTGRAY);
        String s = "LSystems";
        DrawText(s, screenWidth-screenWidth/3+(screenWidth/3)/2-MeasureText(s, 24)/2, 10, 24, BLACK);
        s = "press right arrow to step";
        DrawText(s, screenWidth-screenWidth/3+(screenWidth/3)/2-MeasureText(s, 14)/2, 50, 14, BLACK);
        s = "Loaded L-system:";
        DrawText(s, screenWidth-screenWidth/3+(screenWidth/3)/2-MeasureText(s, 22)/2, 100, 22, BLACK);
        s = this.system.prettyString();
        DrawText(s, screenWidth-screenWidth/3+(screenWidth/3)/2-MeasureText(s, 20)/2, 130, 20, BLACK);
        s = "Guidelines:";
        DrawText(s, screenWidth-screenWidth/3+(screenWidth/3)/2-MeasureText(s, 22)/2, 280, 22, BLACK);
        s = "X,Y: Line\n\n[,]: Separation\n\n-,+: Rotate " + this.minus_angle + "°/" + this.plus_angle + "°";
        DrawText(s, screenWidth-screenWidth/3+(screenWidth/3)/2-MeasureText(s, 20)/2, 310, 20, BLACK);

        EndDrawing();
    }

    public LSystem getSystem() {
        return system;
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public static void main(String[] args) {
        Main main = new Main();
        if (args.length >= 1) {
            Optional<String> opt = main.parse(args[0]);
            if (opt.isPresent()) {
                System.err.println(opt.get());
                System.exit(1);
            }
        }

        InitWindow(Main.screenWidth, Main.screenHeight, "LSystems");
        TraceLog(LOG_INFO, "Java Version: " + Runtime.version().toString());
        SetTargetFPS(60);
        while(!WindowShouldClose()) {
            main.update();
            main.draw();
        }
        CloseWindow();
    }
}
