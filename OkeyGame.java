import java.util.ArrayList;
import java.util.Random;

public class OkeyGame {

    Player[] players;
    Tile[] tiles;


    
    int discarted_index;
    ArrayList <Tile>  Board_Tiles= new ArrayList<>(); 

    Tile lastDiscardedTile;

    int currentPlayerIndex = -1;
    
    public OkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[104];
        int currentTile = 0;

        // two copies of each color-value combination, no jokers
        for (int i = 1; i <= 13; i++) {
            for (int j = 0; j < 2; j++) {
                tiles[currentTile++] = new Tile(i,'Y');
                tiles[currentTile++] = new Tile(i,'B');
                tiles[currentTile++] = new Tile(i,'R');
                tiles[currentTile++] = new Tile(i,'K');
            }
        }
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles
     * this method assumes the tiles are already sorted
     */
    public void distributeTilesToPlayers() {
        shuffleTiles();
        for(int i=0;i<4;i++){
            if(i==0){
                for(int k=0;k<15;k++){
                    players[0].playerTiles[k]=this.tiles[k];
                }
            }
            else{
                for(int k=0;k<14;k++){
                    players[i].playerTiles[k]=this.tiles[k+14*i+1];
                }
            }
        }
        for(int i=57;i<104;i++){
            this.Board_Tiles.add(tiles[i]);
        }
        /* 
        for (Player player : players) {
            
        }
        */
    }
    

    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getLastDiscardedTile() {
        players[currentPlayerIndex].playerTiles[14]=lastDiscardedTile;
        return lastDiscardedTile.toString();
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getTopTile() {
        Tile t;
        t=Board_Tiles.get(Board_Tiles.size()-1);
        Board_Tiles.remove(Board_Tiles.size()-1);
        players[currentPlayerIndex].playerTiles[14]=t;
        return t.toString();
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     */
    public void shuffleTiles() {
        Random r=new Random();
        int ran_num;
        Tile Temp_Tile;
        for(int i=0;i<104;i++){
            ran_num=r.nextInt(104);
            Temp_Tile=tiles[ran_num];
            tiles[ran_num]=tiles[i];
            tiles[i]=Temp_Tile;
        }
    }

    /*
     * TODO: check if game still continues, should return true if current player
     * finished the game. Use calculateLongestChainPerTile method to get the
     * longest chains per tile.
     * To win, you need one of the following cases to be true:
     * - 8 tiles have length >= 4 and remaining six tiles have length >= 3 the last one can be of any length
     * - 5 tiles have length >= 5 and remaining nine tiles have length >= 3 the last one can be of any length
     * These are assuming we check for the win condition before discarding a tile
     * The given cases do not cover all the winning hands based on the original
     * game and for some rare cases it may be erroneous but it will be enough
     * for this simplified version
     */
    public boolean didGameFinish() {
        this.currentPlayerIndex+=1;
        this.currentPlayerIndex=this.currentPlayerIndex%4;
        
        
        int[] chain=new int[14];
        int[] chain_nums=new int[3];
        chain=players[currentPlayerIndex].calculateLongestChainPerTile();
        for(int i=0;i<14;i++){
             if(chain[i]>=5){
                chain_nums[0]++;
             }
             else if(chain[i]>=4){
                chain_nums[1]++;
             }
             else if(chain[i]>=3){
                chain_nums[2]++;
             }
        }
        
        if((chain_nums[0]==5)&&(chain_nums[2]==8)){
            return true;
        }
        else if((chain_nums[1]==8)&&(chain_nums[2]==5)){
            return true;
        }
        else{
            return false;
        }
        
        
    }

    /*
     * TODO: Pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * You may choose randomly or consider if the discarded tile is useful for
     * the current status. Print whether computer picks from tiles or discarded ones.
     */
    public void pickTileForComputer() {        
        Random r= new Random();
        int c=r.nextInt(2);
        if(c==0){
            getTopTile();
        }
        else{
            getLastDiscardedTile();
        }
        System.out.println(players[currentPlayerIndex].playerTiles[14]);
    }

    /*
     * TODO: Current computer player will discard the least useful tile.
     * For this use the findLongestChainOf method in Player class to calculate
     * the longest chain length per tile of this player,
     * then choose the tile with the lowest chain length and discard it
     * this method should print what tile is discarded since it should be
     * known by other players
     */
    public void discardTileForComputer() {
        int min=41;
        int min_index=41;
        for(int i=0;i<14;i++){
            if(min>players[currentPlayerIndex].findLongestChainOf(players[currentPlayerIndex].playerTiles[i])){
                min=players[currentPlayerIndex].findLongestChainOf(players[currentPlayerIndex].playerTiles[i]);
                min_index=i;
            }
        }
        discarted_index=min_index;
        lastDiscardedTile=players[currentPlayerIndex].playerTiles[discarted_index];
        players[currentPlayerIndex].playerTiles[min_index]=players[currentPlayerIndex].playerTiles[14];
        players[currentPlayerIndex].playerTiles[14]=null;
    }

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {

        discarted_index=tileIndex;
        lastDiscardedTile=players[0].playerTiles[discarted_index];
        players[0].playerTiles[tileIndex]=players[0].playerTiles[14];
        players[0].playerTiles[14]=null;
    }

    public void currentPlayerSortTilesColorFirst() {
        players[currentPlayerIndex].sortTilesColorFirst();
    }

    public void currentPlayerSortTilesValueFirst() {
        players[currentPlayerIndex].sortTilesValueFirst();
    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        currentPlayerIndex+=1;
        currentPlayerIndex=currentPlayerIndex%4;
        return currentPlayerIndex;
    }

      public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
