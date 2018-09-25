package agents;

import action.SearchLegacy;
import core.*;
import core.Map;

import java.util.*;

public class FireFighter extends SoSObject {

    World world;
    Map localMap;
    Queue<Tile> unVisited;
    LinkedList<Patient> patientsMemory = new LinkedList<>();

    int sightRange = 5;

    public FireFighter(World world, String name) {
        super(name);
        this.world = world;
        addChild(new ImageObject("src/engine/resources/ff30x30.png"));

        localMap = new Map();
        LinkedList<Tile> temp= new LinkedList<>(localMap.getTiles());
        //LinkedList<Tile> temp= new LinkedList<>(world.getMap().getTiles());
        Collections.shuffle(temp);
        unVisited = temp;

        new SearchLegacy(this);
        //search();
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void clear() {
        world = null;
    }

//    @Override
//    public void sendMessage(String msg, Object data) {
//        if(msg.startsWith("action complete")) {
//            search();
//        }
//    }
//
//    public void search() {
//        while(true) {
//            if(unVisited.isEmpty()) break;
//            Tile tile = unVisited.poll();
//            if(tile.isVisited()) continue;
//
//            new MoveToLegacy(this, tile.position);
//            break;
//        }
//    }

    @Override
    public void setPosition(int _x, int _y) {
        super.setPosition(_x, _y);
        //this.position.set(_x, _y);
        for(int y = position.y - sightRange / 2; y <= position.y + sightRange / 2; ++y) {
            for(int x = position.x - sightRange / 2; x <= position.x + sightRange / 2; ++x) {

                Tile worldTile = world.getMap().getTile(x, y);
                if(worldTile != null && worldTile.isVisited() == false) {
                    ArrayList<SoSObject> objects = worldTile.getObjects();
                    objects.forEach(obj -> {
                        if(obj instanceof Patient) {
                            patientsMemory.add((Patient) obj);
                        }
                    });
                }
                localMap.visited(x, y, true);
                world.getMap().visited(x, y, true);
            }
        }
    }

    public Queue<Tile> getUnVisited() {
        return unVisited;
    }

    public LinkedList<Patient> getPatientsMemory() {
        return patientsMemory;
    }
}
