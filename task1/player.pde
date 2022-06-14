PImage heroSet; // Hero tileset image. 
int rowCountH, colCountH; // How many tiles are in each tilemap row/column
PImage[] tilesH; // Store the actual tiles, that will be rendered
int heroX = 0;
int heroY = 0;
int heroXBlock = heroX * tileSize;
int heroYBlock = heroY * tileSize;
int positionIndex = 1;
int flag = 4;
int positionDisplacement = 0;
void initHero(){
  heroSet = loadImage("hero.png");
  rowCountH = heroSet.height / srcTileSize;
  colCountH = heroSet.width / srcTileSize;
  tilesH = new PImage[rowCountH * colCountH];  
  int s = 0;
  for(int row = 0; row < rowCountH; ++row) {
    for(int col = 0; col < colCountH; ++col) {
      tilesH[s++] = heroSet.get(col * srcTileSize, row * srcTileSize, srcTileSize, srcTileSize);
    }
  }
}
void drawPlayer()
{
  image(tilesH[positionIndex], heroXBlock, heroYBlock, tileSize, tileSize);
}
void positionChange(int positionDisplacement)
{
  if (positionIndex < 2+positionDisplacement)
        {
          ++positionIndex;
        }
        else
        {
          positionIndex = 0+positionDisplacement;
        }
}
void checkForWin(int temp)
{
  if ((tileMap[temp] == flag))
    {
      heroXBlock = 0;
      heroYBlock = 0;
      displacementY = 0;
      displacementX = 0;
      levelChange();
    }
}
public void movement()
{
  if (up)
  {
    int col = (int) map(heroXBlock + displacementX, 0, width, 0, realColCount);
    int row = (int) map(heroYBlock - tileSize + displacementY, 0, height, 0, realRowCount);
    int temp = row * realColCount + col;
    if (heroYBlock - tileSize >= 0 
    && ((tileMap[temp] == 0)||(tileMap[temp] == 5)||(tileMap[temp] == 10)))
    {
      if ((heroYBlock - tileSize < 6 * tileSize) && displacementY > 0)
      {
        --displacementY;
        copyMap(displacementY, displacementX);
        positionChange(0);
      }
      else
      {
        heroYBlock = heroYBlock - tileSize;
        positionChange(0);
      }
    }
    if (heroYBlock - tileSize >= 0 )
    {
      checkForWin(temp);
    }
  }
  if (down)
  {
    int col = (int) map(heroXBlock + displacementX, 0, width, 0, realColCount);
    int row = (int) map(heroYBlock + tileSize + displacementY, 0, height, 0, realRowCount);
    int temp = row * realColCount + col;
    if (heroYBlock + tileSize < 13 * tileSize
    && ((tileMap[temp] == 0)||(tileMap[temp] == 5)||(tileMap[temp] == 10)))
    {
      if ((heroYBlock + tileSize > 6 * tileSize) && displacementY < realRowCount)
      {
        ++displacementY;
        copyMap(displacementY, displacementX);
        positionChange(0);
      }
      else
      {
        heroYBlock = heroYBlock + tileSize ;
        positionChange(0);
      }
    }
    if (heroYBlock + tileSize < 13 * tileSize)
    {
      checkForWin(temp);
    }
  }
  if (left)
  {
    int col = (int) map(heroXBlock - tileSize + displacementX, 0, width, 0, realColCount);
    int row = (int) map(heroYBlock + displacementY, 0, height, 0, realRowCount);
    int temp = row * realColCount + col;
    if (heroXBlock - tileSize >= 0
    && ((tileMap[temp] == 0)||(tileMap[temp] == 5)||(tileMap[temp] == 10)))
    {
      if ((heroXBlock - tileSize < 13 * tileSize) && displacementX > 0)
      {
        --displacementX;
        copyMap(displacementY, displacementX);
        positionChange(3);
      }
      else
      {
        heroXBlock = heroXBlock - tileSize ;
        positionChange(3);
      }
    }
    if (heroXBlock - tileSize >= 0)
    {
      checkForWin(temp);
    }
  }
  if (right)
  {
    int col = (int) map(heroXBlock + tileSize + displacementX, 0, width, 0, realColCount);
    int row = (int) map(heroYBlock + displacementY, 0, height, 0, realRowCount);
    int temp = row * realColCount + col;
    if (heroXBlock + tileSize < 25 * tileSize
    && ((tileMap[temp] == 0)||(tileMap[temp] == 5)||(tileMap[temp] == 10)))
    {
      if ((heroXBlock + tileSize > 13 * tileSize) && displacementX < realColCount)
      {
        ++displacementX;
        copyMap(displacementY, displacementX);
        positionChange(3);
      }
      else
      {
        heroXBlock = heroXBlock + tileSize ;
        positionChange(3);
      }
    }
    if (heroXBlock + tileSize < 25 * tileSize)
    {
      checkForWin(temp);
    }
    
  }
}
