PImage tileset; // Castle tileset image. 
int srcTileSize = 32; // How many pixels does each tile take up (in the tileset)
int tileSize = 64; // How many pixels does each tile take up (on the screen)
int rowCount, colCount; // How many tiles are in each tilemap row/column
int realRowCount, realColCount;
int realRowCountBig, realColCountBig;
PImage[] tiles; // Store the actual tiles, that will be rendered
boolean eIsPressed = false;
int chosenTile = 0;  // Chosen tile (in the tiles array) index
int[] tileMap; // Store our game map
int[] tileMapBig; // Store our game map
boolean up = false;
boolean down = false;
boolean left = false;
boolean right = false;
int displacementX = 0;
int displacementY = 0;
int nextLevel = 0;

void setup() {  
  initAssets();
  initHero();
  size(1600, 832);
  frameRate(30);
  fill(255, 0, 0);
  textSize(20);
  levelChange();
}

void draw() {
  drawMap();  
  drawPlayer();
  movement();
  if (eIsPressed)
  {
    image(tiles[chosenTile], mouseX, mouseY, tileSize, tileSize);
  }
}
void keyPressed()
{
 if(key =='a')
 {
   
 }
}
public void keyPressed(KeyEvent e)
{
  char code = e.getKey();
  if (code == 'e')
  {
    eIsPressed = true;
  }
  if (code == 'w')
  {
    up = true;
  }
  if (code == 's')
  {
    down = true;
  }
  if (code == 'a')
  {
    left = true;
  }
  if (code == 'd')
  {
    right = true;
  }
}

public void keyReleased(KeyEvent e)
{
  char code = e.getKey();
  if (code == 'e')
  {
    eIsPressed = false;
  }
  if (code == 'w')
  {
    up = false;
    positionIndex = 1;
  }
  if (code == 's')
  {
    down = false;
    positionIndex = 1;
  }
  if (code == 'a')
  {
    left = false;
    positionIndex = 4;
  }
  if (code == 'd')
  {
    right = false;
    positionIndex = 4;
  }
}

int toIndex(int row, int col) {
   return row * colCount + col;
}

void mousePressed() {
  if (eIsPressed)
  {
    int col = (int) map(mouseX, 0, width, 0, realColCount);
    int row = (int) map(mouseY, 0, height, 0, realRowCount);
    if (col > realColCount || row > realRowCount)
    {
      return;
    }
    tileMapBig[(row+displacementY) * realColCountBig + (col+displacementX)] = chosenTile;
    copyMap(displacementY, displacementX);
  }
}

void mouseDragged() {
  if (eIsPressed)
  {
    int col = (int) map(mouseX, 0, width, 0, realColCount);
    int row = (int) map(mouseY, 0, height, 0, realRowCount);
    if (col > realColCount || row > realRowCount)
    {
      return;
    }
    tileMapBig[(row+displacementY) * realColCountBig + (col+displacementX)] = chosenTile;
    copyMap(displacementY, displacementX);
  }
}

void mouseWheel(MouseEvent event){
  if (eIsPressed)
  {
  chosenTile += event.getCount();
  if (chosenTile < 0)
  {
    chosenTile = 0;
  }
  if (chosenTile > 14)
  {
    chosenTile = 14;
  }
  }
}

void drawMap(){
   for(int row = 0; row < realRowCount; ++row) {
    for(int col = 0; col < realColCount; ++col) {
       int tileIndex = tileMap[row * realColCount + col];
       image(tiles[tileIndex], col * tileSize, row * tileSize, tileSize, tileSize);
    }
  }
}

void copyMap(int y, int x)
{
  for(int row = 0; row < realRowCount; ++row) {
    for(int col = 0; col < realColCount; ++col) {
      tileMap[row * realColCount + col] = tileMapBig[(row+y) * realColCountBig + (col+x)];
    }
  }
}

void levelChange()
{
  if (nextLevel == 0)
  {
    setTileMapForDesert();
    ++nextLevel;
  }
  else if (nextLevel == 1)
  {
    setTileMapForGrass();
    ++nextLevel;
  }
  else if (nextLevel == 2)
  {
    setTileMapForSee();
    nextLevel = 0;
  }
}

void setTileMapForDesert()
{
  for(int row = 0; row < realRowCountBig; ++row) {
    for(int col = 0; col < realColCountBig; ++col) {
      int index = getRandomNumber(0, 5);
      if (index == 0)
      {
        tileMapBig[row * realColCountBig + col] = getRandomNumber(1, 4);
      }
       else
       {
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

void setTileMapForGrass()
{
  for(int row = 0; row < realRowCountBig; ++row) {
    for(int col = 0; col < realColCountBig; ++col) {
       int index = getRandomNumber(0, 5);
      if (index == 0)
      {
        tileMapBig[row * realColCountBig + col] = getRandomNumber(6, 9);
      }
       else
       {
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

void setTileMapForSee()
{
  for(int row = 0; row < realRowCountBig; ++row) {
    for(int col = 0; col < realColCountBig; ++col) {
       int index = getRandomNumber(0, 5);
      if (index == 0)
      {
        tileMapBig[row * realColCountBig + col] = getRandomNumber(11, 14);
      }
       else
       {
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

void initAssets(){
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
  for(int row = 0; row < rowCount; ++row) {
    for(int col = 0; col < colCount; ++col) {
      tiles[s++] = tileset.get(col * srcTileSize, row * srcTileSize, srcTileSize, srcTileSize);
    }
  }
}

public int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
}
