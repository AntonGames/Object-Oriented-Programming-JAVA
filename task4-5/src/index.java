import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.io.IOException;
import java.util.Objects;

public class index extends PApplet {
    static class for_loop {
        int id;
        int loop_beginning_line; // in which line of code does the for loop begin
        int loop_repeat_times;   // how many times will the loop be repeated
        int loop_length;         // how many lines are in the loop
    }

    PImage tileset; // Castle tileset image.
    int srcTileSize = 32; // How many pixels does each tile take up (in the tileset)
    int tileSize = 64; // How many pixels does each tile take up (on the screen)
    int rowCount, colCount; // How many tiles are in each tilemap row/column
    int realRowCount, realColCount;
    int realRowCountBig, realColCountBig;
    PImage[] tiles; // Store the actual tiles, that will be rendered
    boolean eIsPressed = false;
    int chosenTile = 0;  // Chosen tile (in the tiles array) index
    int[] tileMap = new int[500]; // Store our game map
    int[] tileMapBig = new int[500]; // Store our game map
    boolean up = false;
    boolean down = false;
    boolean left = false;
    boolean right = false;
    int displacementX = 0;
    int displacementY = 0;
    int nextLevel = 0;

    for_loop[] fors = new for_loop[500];
    int fors_index = 0;

    PImage heroSet; // Hero tileset image.
    int rowCountH, colCountH; // How many tiles are in each tilemap row/column
    PImage[] tilesH; // Store the actual tiles, that will be rendered
    int heroXBlock = 0;
    int heroYBlock = 0;
    int positionIndex = 1;
    int flag = 4;

    boolean running = false;
    boolean in_while = false;

    String[] code_text;
    static Thread game_main_thread = new Thread(() -> PApplet.main("index"));
    public static void main(String[] args) {

        Thread chat_server_thread = new Thread(() -> {
            try {
                server.main(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        chat_server_thread.start();

        Thread chat_client_thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                client.main(args);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        chat_client_thread.start();

        Thread code_modifier_thread = new Thread(() -> code_modifier.main(args));
        code_modifier_thread.start();

        game_main_thread.start();
    }

    public void setup() {
        code_text = loadStrings("code.txt");
        initAssets();
        initHero();
        levelChange();
        frameRate(30);
        fill(255, 0, 0);
        textSize(20);
    }

    public void settings() {
        size(1600, 832);
    }

    int current_line = 0;

    int repeated_times = 0;
    int lines_in_a_for = 0;

    double timer_count = millis();
    double timer_length = 300;

    boolean temp_curr_changed = false;
    boolean while_com_saved=false;
    String while_com;
    int temp_curr;

    public void draw() {
        drawMap();
        movement();
        drawPlayer();
        if (running) {
            if (millis() - timer_count >= timer_length) {
                if (fors_index == 0) { // NOT IN A LOOP
                    //System.out.println(in_while);
                    if (in_while) {

                        if(!while_com_saved){
                            while_com = get_while_command(current_line-2);
                            while_com_saved = true;
                        }
                        if (while_com.equals("colliding_left()") && !colliding_left()) {in_while = false; current_line++;}
                        if (while_com.equals("colliding_right()") && !colliding_right()) {in_while = false; current_line++;}
                        if (while_com.equals("colliding_up()") && !colliding_up()) {in_while = false; current_line++;}
                        if (while_com.equals("colliding_down()") && !colliding_down()) {in_while = false; current_line++;}
                        if (while_com.equals("!colliding_left()") && colliding_left()) {in_while = false; current_line++;}
                        if (while_com.equals("!colliding_right()") && colliding_right()) {in_while = false; current_line++;}
                        if (while_com.equals("!colliding_up()") && colliding_up()) {in_while = false; current_line++;}
                        if (while_com.equals("!colliding_down()") && colliding_down()) {in_while = false; current_line++;}



                        if(!temp_curr_changed) {
                            temp_curr = current_line;
                            temp_curr_changed = true;
                        }

                        System.out.println(temp_curr);
                        scan_code_file(temp_curr);

                        //scan_code_file(temp_curr-1);
                    }else{
                        scan_code_file(current_line);
                        current_line++;
                    }
                    //current_line++;
                } else { // IN A LOOP
                    if (repeated_times < fors[fors_index-1].loop_repeat_times) {
                        if (lines_in_a_for < fors[fors_index - 1].loop_length) {
                            System.out.println("reading");
                            scan_code_file(current_line);
                            current_line++;
                            lines_in_a_for++;
                        } else {
                            if (repeated_times != fors[fors_index-1].loop_repeat_times - 1) {
                                lines_in_a_for = 0;
                                current_line -= fors[fors_index - 1].loop_length;
                            }
                            System.out.println(code_text[current_line]);
                            repeated_times++;
                            System.out.println(repeated_times);
                        }
                    } else {
                        fors_index = 0;
                        lines_in_a_for = 0;
                        repeated_times = 0;
                    }
                }
                if (current_line == code_text.length)
                    running = false;
                ///////////////////////
                timer_count = millis();
            }
        }
    }

    void drawPlayer() {
        image(tilesH[positionIndex], heroXBlock, heroYBlock, tileSize, tileSize);
    }

    void initHero() {
        heroSet = loadImage("hero.png");
        rowCountH = heroSet.height / srcTileSize;
        colCountH = heroSet.width / srcTileSize;
        tilesH = new PImage[rowCountH * colCountH];
        int s = 0;
        for (int row = 0; row < rowCountH; ++row) {
            for (int col = 0; col < colCountH; ++col) {
                tilesH[s++] = heroSet.get(col * srcTileSize, row * srcTileSize, srcTileSize, srcTileSize);
            }
        }
    }

    public void read_code_file() {
        code_text = loadStrings("code.txt");
        println("There are " + code_text.length + " lines of code.");
    }

    public void scan_code_file(int index) {
        // SIMPLE COMMANDS
        if (code_text[index].equals("go_left();")) left = true;
        if (code_text[index].equals("go_right();")) right = true;
        if (code_text[index].equals("go_up();")) up = true;
        if (code_text[index].equals("go_down();")) down = true;

        if (code_text[index].contains("<")){
            if (Objects.equals(flag_less_from(index), "index_right()")){
                int col = (int) map(heroXBlock+tileSize, 0, width, 0, realColCount);
                int row = (int) map(heroYBlock, 0, height, 0, realRowCount);
                if (col > realColCount || row > realRowCount) {
                    return;
                }
                if (tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] < flag_less_than(index))
                    put_flag_right();
            } else if (Objects.equals(flag_less_from(index), "index_left()")){
                int col = (int) map(heroXBlock-tileSize, 0, width, 0, realColCount);
                int row = (int) map(heroYBlock, 0, height, 0, realRowCount);
                if (col > realColCount || row > realRowCount) {
                    return;
                }
                if (tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] < flag_less_than(index))
                    put_flag_left();
            } else if (Objects.equals(flag_less_from(index), "index_down()")){
                int col = (int) map(heroXBlock, 0, width, 0, realColCount);
                int row = (int) map(heroYBlock+tileSize, 0, height, 0, realRowCount);
                if (col > realColCount || row > realRowCount) {
                    return;
                }
                if (tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] < flag_less_than(index))
                    put_flag_down();
            } else if (Objects.equals(flag_less_from(index), "index_up()")){
                int col = (int) map(heroXBlock, 0, width, 0, realColCount);
                int row = (int) map(heroYBlock-tileSize, 0, height, 0, realRowCount);
                if (col > realColCount || row > realRowCount) {
                    return;
                }
                if (tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] < flag_less_than(index))
                    put_flag_up();
            }
            current_line++;
        }

        if (code_text[index].contains(">")){
            if (Objects.equals(flag_more_from(index), "index_right()")){
                int col = (int) map(heroXBlock+tileSize, 0, width, 0, realColCount);
                int row = (int) map(heroYBlock, 0, height, 0, realRowCount);
                if (col > realColCount || row > realRowCount) {
                    return;
                }
                if (tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] > flag_more_than(index))
                    put_empty_right();
            } else if (Objects.equals(flag_more_from(index), "index_left()")){
                int col = (int) map(heroXBlock-tileSize, 0, width, 0, realColCount);
                int row = (int) map(heroYBlock, 0, height, 0, realRowCount);
                if (col > realColCount || row > realRowCount) {
                    return;
                }
                if (tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] > flag_more_than(index))
                    put_empty_left();
            } else if (Objects.equals(flag_more_from(index), "index_down()")){
                int col = (int) map(heroXBlock, 0, width, 0, realColCount);
                int row = (int) map(heroYBlock+tileSize, 0, height, 0, realRowCount);
                if (col > realColCount || row > realRowCount) {
                    return;
                }
                if (tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] > flag_more_than(index))
                    put_empty_down();
            } else if (Objects.equals(flag_more_from(index), "index_up()")){
                int col = (int) map(heroXBlock, 0, width, 0, realColCount);
                int row = (int) map(heroYBlock-tileSize, 0, height, 0, realRowCount);
                if (col > realColCount || row > realRowCount) {
                    return;
                }
                if (tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] > flag_more_than(index))
                    put_empty_up();
            }
            current_line++;
        }

        if (code_text[index].equals("put_flag_right();")) put_flag_right();
        if (code_text[index].equals("put_flag_left();")) put_flag_left();
        if (code_text[index].equals("put_flag_down();")) put_flag_down();
        if (code_text[index].equals("put_flag_up();")) put_flag_up();

        if (code_text[index].contains("for")) create_for(index);

        if (code_text[index].contains("if")) {
            if (get_if_command(index).equals("colliding_left()")) {
                if (colliding_left()) {
                    scan_code_file(++index);
                }
                else{
                    current_line++;
                }
            }
            if (get_if_command(index).equals("colliding_right()")) {
                if (colliding_right())
                    scan_code_file(++index);
                else{
                    current_line++;
                }
            }
            if (get_if_command(index).equals("colliding_down()")) {
                if (colliding_down())
                    scan_code_file(++index);
                else{
                    current_line++;
                }
            }
            if (get_if_command(index).equals("colliding_up()")) {
                if (colliding_up())
                    scan_code_file(++index);
                else{
                    current_line++;
                }
            }

            if (get_if_command(index).equals("!colliding_left()")) {
                if (!colliding_left()) {
                    scan_code_file(++index);
                }
                else{
                    current_line++;
                }
            }
            if (get_if_command(index).equals("!colliding_right()")) {
                if (!colliding_right())
                    scan_code_file(++index);
                else{
                    current_line++;
                }
            }
            if (get_if_command(index).equals("!colliding_down()")) {
                if (!colliding_down())
                    scan_code_file(++index);
                else{
                    current_line++;
                }
            }
            if (get_if_command(index).equals("!colliding_up()")) {
                if (!colliding_up())
                    scan_code_file(++index);
                else{
                    current_line++;
                }
            }
        }

        if(code_text[index].contains("while")) {
            System.out.println(get_while_command(index));
            if (get_while_command(index).equals("colliding_left()")) {
                System.out.println("kazas");
                if (colliding_left()) {
                    System.out.println("kazas");
                    in_while = true;
                } else
                    in_while = false;
            }
            if (get_while_command(index).equals("colliding_right()")) {
                if (colliding_right()) {
                    //scan_code_file(++index);
                    in_while = true;
                } else
                    in_while = false;
            }
            if (get_while_command(index).equals("colliding_down()")) {
                if (colliding_down()) {
                    //scan_code_file(++index);
                    in_while = true;
                } else
                    in_while = false;
            }
            if (get_while_command(index).equals("colliding_up()")) {
                if (colliding_up()) {
                    //scan_code_file(++index);
                    in_while = true;
                } else
                    in_while = false;
            }

            if (get_while_command(index).equals("!colliding_left()")) {
                if (!colliding_left()) {
                    //scan_code_file(++index);
                    System.out.println("einam");
                    in_while = true;
                } else
                    in_while = false;
            }
            if (get_while_command(index).equals("!colliding_right()")) {
                if (!colliding_right()) {
                    //scan_code_file(++index);
                    in_while = true;
                } else
                    in_while = false;
            }
            if (get_while_command(index).equals("!colliding_down()")) {
                if (!colliding_down()) {
                    //scan_code_file(++index);
                    System.out.println("einam");
                    in_while = true;
                    current_line++;
                } else
                    in_while = false;
            }
            if (get_while_command(index).equals("!colliding_up()")) {
                if (!colliding_up()) {
                    //scan_code_file(++index);
                    in_while = true;
                } else
                    in_while = false;
            }
        }
    }

    public void put_empty_right(){
        int col = (int) map(heroXBlock+tileSize, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock, 0, height, 0, realRowCount);
        if (col > realColCount || row > realRowCount) {
            return;
        }
        if (nextLevel==0)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 10;
        else if (nextLevel==1)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 0;
        else if (nextLevel==2) {
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 5;
            //nextLevel = 0;
        }
        copyMap(displacementY, displacementX);
    }

    public void put_empty_left(){
        int col = (int) map(heroXBlock-tileSize, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock, 0, height, 0, realRowCount);
        if (col > realColCount || row > realRowCount) {
            return;
        }
        if (nextLevel==0)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 10;
        else if (nextLevel==1)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 0;
        else if (nextLevel==2) {
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 5;
            //nextLevel = 0;
        }
        copyMap(displacementY, displacementX);
    }

    public void put_empty_down(){
        int col = (int) map(heroXBlock, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock+tileSize, 0, height, 0, realRowCount);
        if (col > realColCount || row > realRowCount) {
            return;
        }
        if (nextLevel==0)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 10;
        else if (nextLevel==1)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 0;
        else if (nextLevel==2) {
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 5;
            //nextLevel = 0;
        }
        copyMap(displacementY, displacementX);
    }

    public void put_empty_up(){
        int col = (int) map(heroXBlock, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock-tileSize, 0, height, 0, realRowCount);
        if (col > realColCount || row > realRowCount) {
            return;
        }
        if (nextLevel==0)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 10;
        else if (nextLevel==1)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 0;
        else if (nextLevel==2) {
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 5;
            //nextLevel = 0;
        }
        copyMap(displacementY, displacementX);
    }

    public String flag_less_from(int index){
        StringBuilder tile_index = new StringBuilder();
        for (int i=3; i<code_text[index].length(); i++){
            if (code_text[index].charAt(i)!= '<')
                tile_index.append(code_text[index].charAt(i));
            else
                break;
        }
        return tile_index.toString();
    }

    public int flag_less_than(int index){
        StringBuilder tile_index = new StringBuilder();

        int where_sign = 0;

        for (int i=0; i<code_text[index].length(); i++){
            if (code_text[index].charAt(i) == '<') where_sign = i;
        }

        for (int i=where_sign+1; i<code_text[index].length(); i++){
            if (code_text[index].charAt(i)!= ')')
                tile_index.append(code_text[index].charAt(i));
            else
                break;
        }
        return Integer.parseInt(tile_index.toString());
    }

    public String flag_more_from(int index){
        StringBuilder tile_index = new StringBuilder();
        for (int i=3; i<code_text[index].length(); i++){
            if (code_text[index].charAt(i)!= '>')
                tile_index.append(code_text[index].charAt(i));
            else
                break;
        }
        return tile_index.toString();
    }

    public int flag_more_than(int index){
        StringBuilder tile_index = new StringBuilder();

        int where_sign = 0;

        for (int i=0; i<code_text[index].length(); i++){
            if (code_text[index].charAt(i) == '>') where_sign = i;
        }

        for (int i=where_sign+1; i<code_text[index].length(); i++){
            if (code_text[index].charAt(i)!= ')')
                tile_index.append(code_text[index].charAt(i));
            else
                break;
        }
        return Integer.parseInt(tile_index.toString());
    }

    private void put_flag_right() {
        int col = (int) map(heroXBlock+tileSize, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock, 0, height, 0, realRowCount);
        if (col > realColCount || row > realRowCount) {
            return;
        }
        if (nextLevel==0)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 14;
        else if (nextLevel==1)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 4;
        else if (nextLevel==2) {
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 9;
            //nextLevel = 0;
        }
        copyMap(displacementY, displacementX);
    }


    private void put_flag_left() {
        int col = (int) map(heroXBlock-tileSize, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock, 0, height, 0, realRowCount);
        if (col > realColCount || row > realRowCount) {
            return;
        }
        if (nextLevel==0)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 14;
        else if (nextLevel==1)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 4;
        else if (nextLevel==2) {
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 9;
            //nextLevel = 0;
        }
        copyMap(displacementY, displacementX);
    }

    private void put_flag_up() {
        int col = (int) map(heroXBlock, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock-tileSize, 0, height, 0, realRowCount);
        if (col > realColCount || row > realRowCount) {
            return;
        }
        if (nextLevel==0)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 14;
        else if (nextLevel==1)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 4;
        else if (nextLevel==2) {
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 9;
            //nextLevel = 0;
        }
        copyMap(displacementY, displacementX);
    }

    private void put_flag_down() {
        int col = (int) map(heroXBlock, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock+tileSize, 0, height, 0, realRowCount);
        if (col > realColCount || row > realRowCount) {
            return;
        }
        if (nextLevel==0)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 14;
        else if (nextLevel==1)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 4;
        else if (nextLevel==2) {
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 9;
            //nextLevel = 0;
        }
        copyMap(displacementY, displacementX);
    }
    private void put_tile_right(int dir) {
        int col = (int) map(heroXBlock+tileSize, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock, 0, height, 0, realRowCount);
        if (col > realColCount || row > realRowCount) {
            return;
        }
        if (nextLevel==dir)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 14;
        else if (nextLevel==dir)
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 4;
        else if (nextLevel==dir) {
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = 9;
            //nextLevel = 0;
        }
        copyMap(displacementY, displacementX);
    }
    public String get_if_command(int index) {
        StringBuilder command = new StringBuilder();
        boolean appending = false;
        for (int i = 0; i < code_text[index].length(); i++) {
            if (appending && i != code_text[index].length() - 1)
                command.append(code_text[index].charAt(i));

            if (i == 2)
                appending = true;
            else if (i == code_text[index].length() - 1)
                appending = false;
        }
        return command.toString();
    }

    public String get_while_command(int index) {
        StringBuilder command = new StringBuilder();
        boolean appending = false;
        for (int i = 0; i < code_text[index].length(); i++) {
            if (appending && i != code_text[index].length() - 1)
                command.append(code_text[index].charAt(i));
            if (i == 5)
                appending = true;
            else if (i == code_text[index].length() - 1)
                appending = false;
        }
        return command.toString();
    }

    public void keyPressed(KeyEvent e) {
        char code = e.getKey();
        if (code == ' ')
            running = true;
        if (code == 'r')
            read_code_file();
        if (code == 'e') {
            eIsPressed = true;
        }
        if (code == 'w') {
            up = true;
            //go_up();
        }
        if (code == 's') {
            down = true;
            //go_down();
        }
        if (code == 'a') {
            left = true;
            //go_left();
        }
        if (code == 'd') {
            right = true;
            //go_right();
        }
    }

    public void keyReleased(KeyEvent e) {
        char code = e.getKey();
        if (code == 'e') {
            eIsPressed = false;
        }
        if (code == 'w') {
            up = false;
            positionIndex = 1;
        }
        if (code == 's') {
            down = false;
            positionIndex = 1;
        }
        if (code == 'a') {
            left = false;
            positionIndex = 4;
        }
        if (code == 'd') {
            right = false;
            positionIndex = 4;
        }
    }

    public void mousePressed() {
        if (eIsPressed) {
            int col = (int) map(mouseX, 0, width, 0, realColCount);
            int row = (int) map(mouseY, 0, height, 0, realRowCount);
            if (col > realColCount || row > realRowCount) {
                return;
            }
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = chosenTile;
            copyMap(displacementY, displacementX);
        }
    }

    public void mouseDragged() {
        if (eIsPressed) {
            int col = (int) map(mouseX, 0, width, 0, realColCount);
            int row = (int) map(mouseY, 0, height, 0, realRowCount);
            if (col > realColCount || row > realRowCount) {
                return;
            }
            tileMapBig[(row + displacementY) * realColCountBig + (col + displacementX)] = chosenTile;
            copyMap(displacementY, displacementX);
        }
    }

    public void mouseWheel(MouseEvent event) {
        if (eIsPressed) {
            chosenTile += event.getCount();
            if (chosenTile < 0) {
                chosenTile = 0;
            }
            if (chosenTile > 14) {
                chosenTile = 14;
            }
        }
    }

    void drawMap() {
        for (int row = 0; row < realRowCount; ++row) {
            for (int col = 0; col < realColCount; ++col) {
                int tileIndex = tileMap[row * realColCount + col];
                image(tiles[tileIndex], col * tileSize, row * tileSize, tileSize, tileSize);
            }
        }
    }

    void copyMap(int y, int x) {
        for (int row = 0; row < realRowCount; ++row) {
            for (int col = 0; col < realColCount; ++col) {
                tileMap[row * realColCount + col] = tileMapBig[(row + y) * realColCountBig + (col + x)];
            }
        }
    }

    void levelChange() {
        current_line = 0;
        if (nextLevel == 0) {
            setTileMapForDesert();
            ++nextLevel;
        } else if (nextLevel == 1) {
            setTileMapForGrass();
            ++nextLevel;
        } else if (nextLevel == 2) {
            setTileMapForSee();
            nextLevel = 0;
        }
    }

    void setTileMapForDesert() {
        for (int row = 0; row < realRowCountBig; ++row) {
            for (int col = 0; col < realColCountBig; ++col) {
                int index = getRandomNumber(0, 5);
                if (index == 0) {
                    tileMapBig[row * realColCountBig + col] = getRandomNumber(1, 4);
                } else {
                    tileMapBig[row * realColCountBig + col] = 0;
                }
            }
        }
        int row = getRandomNumber(realRowCount, realRowCountBig);
        int col = getRandomNumber(realColCount, realColCountBig);
        tileMapBig[row * realColCountBig + col] = 4;
        tileMapBig[0] = 0;
        copyMap(0, 0);
        flag = 4;
    }

    void setTileMapForGrass() {
        for (int row = 0; row < realRowCountBig; ++row) {
            for (int col = 0; col < realColCountBig; ++col) {
                int index = getRandomNumber(0, 5);
                if (index == 0) {
                    tileMapBig[row * realColCountBig + col] = getRandomNumber(6, 9);
                } else {
                    tileMapBig[row * realColCountBig + col] = 5;
                }
            }
        }
        int row = getRandomNumber(realRowCount, realRowCountBig);
        int col = getRandomNumber(realColCount, realColCountBig);
        tileMapBig[row * realColCountBig + col] = 9;
        tileMapBig[0] = 5;
        copyMap(0, 0);
        flag = 9;

    }

    void setTileMapForSee() {
        for (int row = 0; row < realRowCountBig; ++row) {
            for (int col = 0; col < realColCountBig; ++col) {
                int index = getRandomNumber(0, 5);
                if (index == 0) {
                    tileMapBig[row * realColCountBig + col] = getRandomNumber(11, 14);
                } else {
                    tileMapBig[row * realColCountBig + col] = 10;
                }
            }
        }
        int row = getRandomNumber(realRowCount, realRowCountBig);
        int col = getRandomNumber(realColCount, realColCountBig);
        tileMapBig[row * realColCountBig + col] = 14;
        tileMapBig[0] = 10;
        copyMap(0, 0);
        flag = 14;
    }

    void initAssets() {
        tileset = loadImage("world.png");
        rowCount = tileset.height / srcTileSize;
        colCount = tileset.width / srcTileSize;
        realRowCount = height / tileSize;
        realColCount = width / tileSize;
        realRowCountBig = height / tileSize * 2;
        realColCountBig = width / tileSize * 2;
        tiles = new PImage[rowCount * colCount];
        tileMap = new int[realRowCount * realColCount];
        tileMapBig = new int[realRowCountBig * realColCountBig];
        int s = 0;
        for (int row = 0; row < rowCount; ++row) {
            for (int col = 0; col < colCount; ++col) {
                tiles[s++] = tileset.get(col * srcTileSize, row * srcTileSize, srcTileSize, srcTileSize);
            }
        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    void positionChange(int positionDisplacement) {
        if (positionIndex < 2 + positionDisplacement) {
            ++positionIndex;
        } else {
            positionIndex = positionDisplacement;
        }
    }

    void checkForWin(int temp) {
        if ((tileMap[temp] == flag)) {
            heroXBlock = 0;
            heroYBlock = 0;
            displacementY = 0;
            displacementX = 0;
            levelChange();
        }
    }

    public void create_for(int index) {
        StringBuilder loop_repeat_times_string = new StringBuilder();
        StringBuilder loop_length_string = new StringBuilder();
        boolean appending_repeat = false;
        boolean appending_length = false;
        for (int i = 0; i < code_text[index].length(); i++) {
            if (appending_repeat && code_text[index].charAt(i) != ':')
                loop_repeat_times_string.append(code_text[index].charAt(i));
            else if (appending_length && code_text[index].charAt(i) != ')')
                loop_length_string.append(code_text[index].charAt(i));

            if (i == 3)
                appending_repeat = true;
            if (code_text[index].charAt(i) == ':') {
                appending_repeat = false;
                appending_length = true;
            }
            if (i == code_text[index].length() - 1)
                appending_length = false;
        }

        System.out.println(loop_repeat_times_string);
        System.out.println(loop_length_string);

        fors[fors_index] = new for_loop();
        fors[fors_index].id = fors_index;
        fors[fors_index].loop_beginning_line = index + 1;
        fors[fors_index].loop_repeat_times = Integer.parseInt(loop_repeat_times_string.toString());
        fors[fors_index].loop_length = Integer.parseInt(loop_length_string.toString());
        fors_index++;
    }

    public void movement() {
        if (up)
            go_up();
        if (down)
            go_down();
        if (left)
            go_left();
        if (right)
            go_right();
    }

    public void go_up() {
        up = false;
        int col = (int) map(heroXBlock + displacementX, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock - tileSize + displacementY, 0, height, 0, realRowCount);
        int temp = row * realColCount + col;
        if (heroYBlock - tileSize >= 0 && ((tileMap[temp] == 0) || (tileMap[temp] == 5) || (tileMap[temp] == 10))) {
            if ((heroYBlock - tileSize < 6 * tileSize) && displacementY > 0) {
                --displacementY;
                copyMap(displacementY, displacementX);
                positionChange(0);
            } else {
                heroYBlock = heroYBlock - tileSize;
                positionChange(0);
            }
        }
        if (heroYBlock - tileSize >= 0) {
            checkForWin(temp);
        }
        up = false;
    }

    public void go_down() {
        down = false;
        int col = (int) map(heroXBlock + displacementX, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock + tileSize + displacementY, 0, height, 0, realRowCount);
        int temp = row * realColCount + col;
        if (heroYBlock + tileSize < 13 * tileSize && ((tileMap[temp] == 0) || (tileMap[temp] == 5) || (tileMap[temp] == 10))) {
            if ((heroYBlock + tileSize > 6 * tileSize) && displacementY < realRowCount) {
                ++displacementY;
                copyMap(displacementY, displacementX);
                positionChange(0);
            } else {
                heroYBlock = heroYBlock + tileSize;
                positionChange(0);
            }
        }
        if (heroYBlock + tileSize < 13 * tileSize) {
            checkForWin(temp);
        }
    }

    public void go_left() {
        left = false;
        int col = (int) map(heroXBlock - tileSize + displacementX, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock + displacementY, 0, height, 0, realRowCount);
        int temp = row * realColCount + col;
        if (heroXBlock - tileSize >= 0 && ((tileMap[temp] == 0) || (tileMap[temp] == 5) || (tileMap[temp] == 10))) {
            if ((heroXBlock - tileSize < 13 * tileSize) && displacementX > 0) {
                --displacementX;
                copyMap(displacementY, displacementX);
                positionChange(3);
            } else {
                heroXBlock = heroXBlock - tileSize;
                positionChange(3);
            }
        }
        if (heroXBlock - tileSize >= 0) {
            checkForWin(temp);
        }
    }

    public void go_right() {
        right = false;
        int col = (int) map(heroXBlock + tileSize + displacementX, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock + displacementY, 0, height, 0, realRowCount);
        int temp = row * realColCount + col;
        if (heroXBlock + tileSize < 25 * tileSize && ((tileMap[temp] == 0) || (tileMap[temp] == 5) || (tileMap[temp] == 10))) {
            if ((heroXBlock + tileSize > 13 * tileSize) && displacementX < realColCount) {
                ++displacementX;
                copyMap(displacementY, displacementX);
                positionChange(3);
            } else {
                heroXBlock = heroXBlock + tileSize;
                positionChange(3);
            }
        }
        if (heroXBlock + tileSize < 25 * tileSize) {
            checkForWin(temp);
        }
    }

    public boolean colliding_left() {
        int col = (int) map(heroXBlock - tileSize + displacementX, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock + displacementY, 0, height, 0, realRowCount);
        int temp = row * realColCount + col;
        return heroXBlock - tileSize < 0 || ((tileMap[temp] != 0) && (tileMap[temp] != 5) && (tileMap[temp] != 10));
    }

    public boolean colliding_right() {
        int col = (int) map(heroXBlock + tileSize + displacementX, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock + displacementY, 0, height, 0, realRowCount);
        int temp = row * realColCount + col;
        return heroXBlock + tileSize >= 25 * tileSize || ((tileMap[temp] != 0) && (tileMap[temp] != 5) && (tileMap[temp] != 10));
    }

    public boolean colliding_up() {
        int col = (int) map(heroXBlock + displacementX, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock - tileSize + displacementY, 0, height, 0, realRowCount);
        int temp = row * realColCount + col;
        return heroYBlock - tileSize < 0 || ((tileMap[temp] != 0) && (tileMap[temp] != 5) && (tileMap[temp] != 10));
    }

    public boolean colliding_down() {
        int col = (int) map(heroXBlock + displacementX, 0, width, 0, realColCount);
        int row = (int) map(heroYBlock + tileSize + displacementY, 0, height, 0, realRowCount);
        int temp = row * realColCount + col;
        return heroYBlock + tileSize >= 13 * tileSize || ((tileMap[temp] != 0) && (tileMap[temp] != 5) && (tileMap[temp] != 10));
    }
}

