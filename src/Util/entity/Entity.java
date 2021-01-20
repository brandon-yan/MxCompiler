package Util.entity;


abstract public class Entity {
    public String name;

    public Entity() {
        this.name = null;
    }


    public Entity(String name) {
        this.name = name;
    }
}