import model.*;

public final class MyStrategy implements Strategy {
    @Override
    public void move(Car self, World world, Game game, Move move) {
        move.setEnginePower(1.0D);
        move.setThrowProjectile(true);
        move.setSpillOil(true);


        if (world.getTick()>180) {
            TileType[][] arraysTilesType = world.getTilesXY();

            int[][] wayP = world.getWaypoints();

            int xx = self.getNextWaypointX();
            int yy = self.getNextWaypointY();

            double xxx = self.getX();
            double yyy = self.getY();


            TileType[][] tilesXY = world.getTilesXY();
            int findX = -1;
            int findY = -1;
            for (int i = 0; i < tilesXY.length; i++) {
                for (int j = 0; j < tilesXY.length; j++) {
                    if (i * game.getTrackTileSize() < xxx && (i + 1) * game.getTrackTileSize() > xxx && j * game.getTrackTileSize() < yyy && (j + 1) * game.getTrackTileSize() > yyy) {
                        findX = i;
                        findY = j;
                    }
                }
                if (findX >= 0 || findY >= 0) {
                    break;
                }
            }
            TileType typeCarTile = tilesXY[findX][findY];
            if (typeCarTile == TileType.LEFT_TOP_CORNER){
                move.setWheelTurn(1);
                move.setBrake(true);
            }
            if (world.getTick() == 10) {
                int a = 10;
            }
        }
        if (world.getTick() > game.getInitialFreezeDurationTicks()) {
            move.setUseNitro(true);
        }
    }
}
