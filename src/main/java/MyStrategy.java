import model.*;

import java.util.ArrayList;



public final class MyStrategy implements Strategy {
    class Coord{
        public Coord(int x, int y) {
            X = x;
            Y = y;
        }
        public int X;
        public int Y;

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Coord other = (Coord) obj;
            if (X != other.X)
                return false;
            if (Y != other.Y)
                return false;
            return true;
        }
    }

    @Override
    public void move(Car self, World world, Game game, Move move) {
        move.setEnginePower(1.0D);
        //move.setThrowProjectile(true);
        //move.setSpillOil(true);


        if (world.getTick()>180) {
            TileType[][] arraysTilesType = world.getTilesXY();

            int[][] wayP = world.getWaypoints();


            ArrayList<Coord> arrayList = new ArrayList<Coord>();
            arrayList.add(new Coord(wayP[0][0], wayP[0][1]));

            ArrayList<Coord> nextRange = new ArrayList<Coord>();
            ArrayList<Coord> rangeTop = new ArrayList<Coord>();
            ArrayList<Coord> rangeRight = new ArrayList<Coord>();
            ArrayList<Coord> rangeBottom = new ArrayList<Coord>();
            ArrayList<Coord> rangeLeft = new ArrayList<Coord>();

            ArrayList<Coord> fullWayPoint = new ArrayList<Coord>();


            int index = 1;
            Coord oldNewPoint = new Coord(wayP[0][0], wayP[0][1]);
            Coord newPoint = null;
            fullWayPoint.add(oldNewPoint);
            while (index < wayP.length+1){
                if (newPoint != null) {
                    oldNewPoint = newPoint;
                }
                if (index == wayP.length){
                    newPoint = new Coord(wayP[0][0], wayP[0][1]);
                }else {
                    newPoint = new Coord(wayP[index][0], wayP[index][1]);
                }
                while (true) {
                    addToList(rangeTop, 0, -1, oldNewPoint, arraysTilesType.length);
                    addToList(rangeRight, 1, 0, oldNewPoint, arraysTilesType.length);
                    addToList(rangeBottom, 0, 1, oldNewPoint, arraysTilesType.length);
                    addToList(rangeLeft, -1, 0, oldNewPoint, arraysTilesType.length);
                    if (!rangeTop.isEmpty() && rangeTop.get(rangeTop.size()-1).equals(newPoint)){
                        nextRange = rangeTop;
                        break;
                    }
                    if (!rangeRight.isEmpty() && rangeRight.get(rangeRight.size()-1).equals(newPoint)){
                        nextRange = rangeRight;
                        break;
                    }
                    if (!rangeBottom.isEmpty() && rangeBottom.get(rangeBottom.size()-1).equals(newPoint)){
                        nextRange = rangeBottom;
                        break;
                    }
                    if (!rangeLeft.isEmpty() && rangeLeft.get(rangeLeft.size()-1).equals(newPoint)){
                        nextRange = rangeLeft;
                        break;
                    }
                }
                fullWayPoint.addAll(nextRange);
                nextRange.clear();
                rangeTop.clear();
                rangeRight.clear();
                rangeBottom.clear();
                rangeLeft.clear();
                index++;
            }



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
            int carIndex = -1;
            for (int i=0; i<fullWayPoint.size(); i++) {
                if ((fullWayPoint.get(i).X == findX) && (fullWayPoint.get(i).Y == findY)){
                    carIndex = i;
                    break;
                }
            }
            if( carIndex >= 0){
                TileType typeCarTilePlus = tilesXY[fullWayPoint.get(carIndex+1).X][fullWayPoint.get(carIndex+1).Y];
                if (typeCarTilePlus == TileType.LEFT_TOP_CORNER){
                    move.setWheelTurn(1);
                    move.setEnginePower(0.1D);
                }
                else if (typeCarTilePlus == TileType.LEFT_BOTTOM_CORNER){
                    move.setWheelTurn(1);
                    move.setEnginePower(0.1D);
                }
                else if (typeCarTilePlus == TileType.RIGHT_BOTTOM_CORNER){
                    move.setWheelTurn(1);
                    move.setEnginePower(0.1D);
                }
                else if (typeCarTilePlus == TileType.RIGHT_TOP_CORNER){
                    move.setWheelTurn(1);
                    move.setEnginePower(0.1D);
                }
                else{
                    TileType typeCarTile = tilesXY[findX][findY];
                    if (typeCarTile == TileType.LEFT_TOP_CORNER &&
                        typeCarTile == TileType.LEFT_BOTTOM_CORNER &&
                        typeCarTile == TileType.RIGHT_BOTTOM_CORNER &&
                        typeCarTile == TileType.RIGHT_TOP_CORNER) {
                        move.setWheelTurn(1);
                        move.setBrake(false);
                    }

                    if (typeCarTile == TileType.HORIZONTAL) {
                        double angle = self.getAngle();
                        double eps = 0.01;
                        if (Math.abs(angle) > eps) {
                            if (angle>0){
                                if (angle > Math.PI /2 && angle < Math.PI){
                                    move.setWheelTurn(0.5);
                                }else{
                                    move.setWheelTurn(-0.5);
                                }
                            }else if (angle <0 ){
                                if (Math.abs(angle) > Math.PI /2 && Math.abs(angle) < Math.PI){
                                    move.setWheelTurn(-0.5);
                                }else{
                                    move.setWheelTurn(0.5);
                                }

                            }
                        }else{
                            move.setWheelTurn(0);
                        }
                    }
                    if (typeCarTile == TileType.VERTICAL) {
                        double angle = self.getAngle();
                        double eps = 0.01;

                        if (Math.abs((Math.abs(angle) - Math.PI /2))  >  eps) {
                            if (angle>0){
                                if (Math.PI /2 - Math.abs(angle) > eps){
                                    move.setWheelTurn(0.5);
                                }else if (Math.PI /2 - Math.abs(angle) < eps ){
                                    move.setWheelTurn(-0.5);
                                }
                            }else{
                                if (Math.PI /2 - Math.abs(angle) > eps){
                                    move.setWheelTurn(-0.5);
                                }else if (Math.PI /2 - Math.abs(angle) < eps ){
                                    move.setWheelTurn(0.5);
                                }
                            }


                        }else{
                            move.setWheelTurn(0);
                        }
                    }
                    move.setEnginePower(1.0D);
                }
            }



            /*if (typeCarTile == TileType.LEFT_TOP_CORNER){
                move.setWheelTurn(1);
                move.setBrake(true);
            }
            if (world.getTick() == 10) {
                int a = 10;
            }*/
        }
        if (world.getTick() > game.getInitialFreezeDurationTicks()) {
            move.setUseNitro(true);
        }
    }

    private void addToList(ArrayList<Coord> aList, int dx, int dy, Coord startWP, int sizeMap){
        if (aList.isEmpty()){
            if (startWP.X + dx >= 0 && startWP.X + dx <= sizeMap && startWP.Y + dy >= 0 && startWP.Y + dy <= sizeMap){
                aList.add(new Coord(startWP.X + dx, startWP.Y + dy));
            }else{
                return;
            }
        }else{
            Coord lastElement = aList.get(aList.size()-1);
            if (lastElement.X + dx >= 0 && lastElement.X + dx <= sizeMap && lastElement.Y + dy >= 0 && lastElement.Y + dy <= sizeMap){
                aList.add(new Coord(lastElement.X + dx, lastElement.Y + dy));
            }else{
                return;
            }
        }
    }
}
