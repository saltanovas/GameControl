package com.game.framework.entities;

import com.game.framework.resources.Resources;
import com.game.framework.utils.MathHelper;
import com.game.world.Tile;

import java.awt.*;

/**
 * defines enemy
 */
public class Player extends Entity {

    private int hp;
    private int maxHp;
    private byte regenDelay;
    private int armor;
    private int gold;

    private byte attackTime;
    private byte damageTime;

    /**
     * sets random possition for player
     * assigns variables
     */
    public Player() {
        super(Resources.PLAYER, MathHelper.randomInt(2, 14), MathHelper.randomInt(2, 7));
        this.hp = 20;
        this.maxHp = 20;
        this.regenDelay = 0;
        this.armor = 10;
        this.gold = 0;
        this.attackTime = 0;
        this.damageTime = 0;
    }

    public void replaceRandomly() {
        super.x = MathHelper.randomInt(2, 14) * Tile.SIZE;
        super.y = MathHelper.randomInt(2, 7) * Tile.SIZE;
    }

    public int getHp() {
        return hp;
    }

    public void instantHeal(int amount) {
        this.hp += amount;
        if (this.hp > this.maxHp) this.hp = this.maxHp;
    }

    public void regenerateHealth() {
        if (this.hp < this.maxHp) this.regenDelay++;
        else this.regenDelay = 0;

        if (this.regenDelay == 70) {
            this.hp++;
            this.regenDelay = 0;
        }
    }

    public void restartPlayer(){
        this.instantHeal(30);
        if (this.getArmor() < 10) {
            this.addArmor(10 - this.getArmor());
        }
    }

    public int getArmor() {
        return armor;
    }

    public void addArmor(int amount) {
        this.armor += amount;
    }

    public int getGold() {
        return gold;
    }

    public void giveGold(int amount) {
        this.gold += amount;
    }

    @Override
    public void move() {
            super.move();
        switch (super.facing) {
            case NORTH:
                super.entityID = Resources.PLAYER_BACK;
                break;
            case SOUTH:
                super.entityID = Resources.PLAYER;
                break;
            case WEST:
                super.entityID = Resources.PLAYER_LEFT;
                break;
            case EAST:
                super.entityID = Resources.PLAYER_RIGHT;
                break;
        }
    }

    public void decreaseTime() {
        if (this.attackTime > 0) this.attackTime--;
        if (this.damageTime > 0) this.damageTime--;
    }

    public void attack() {
        if (this.attackTime == 0) this.attackTime = 30;
    }

    public Rectangle getAttackBox() {
        if (this.attackTime == 20) {
            switch (super.facing) {
                case NORTH:
                    return new Rectangle(super.x, super.y - super.height, super.width, super.height);
                case SOUTH:
                    return new Rectangle(super.x, super.y + super.height, super.width, super.height);
                case WEST:
                    return new Rectangle(super.x - super.width, super.y, super.width, super.height);
                case EAST:
                    return new Rectangle(super.x + super.width, super.y, super.width, super.height);
                default:
                    break;
            }
        }
        return new Rectangle(0, 0, 0, 0);
    }

    /**
     * 1. draws player
     * 2. considers animation
     *
     * @param graphics
     */
    @Override
    public void render(Graphics graphics) {
        if (up || down || left || right && attackTime == 0) {
            this.animationDelay++;
            if (this.animationDelay == 70) {
                this.animationFrame = 1;
            }

            if (super.animationDelay == 140) {
                super.animationDelay = 0;
                super.animationFrame = 2;
            }
        }
        //graphics.setColor(Color.BLUE);
        //graphics.drawRect(x, y, width, height);
        graphics.drawImage(Resources.TEXTURES.get(entityID + animationFrame), super.x, super.y, super.width, super.height, null);
    }

    /**
     * takes damage to player. Considers if player has armor or no
     *
     * @param amount - damage amount
     */
    public void damage(int amount) {
        if (this.damageTime == 0) {
            if (this.armor > 0) this.armor -= amount;
            else{
                this.hp -= amount;
                this.armor = 0;
            }
            this.damageTime = 50;
        }
    }
}
