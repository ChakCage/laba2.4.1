public interface Unit {
    boolean isDead();

    int getCurrentX();


    int getCurrentY();


    void setCurrentX(int x);
    void setCurrentY(int y);
    boolean canMove(int newX, int newY);
    int getCost();
    void setCost(int cost);



    String getName();

    boolean canAttackEnemy(int targetX, int targetY);

    void attackEnemy(Unit targetUnit);

    int getDefense();

   void receiveDamage(int damage);

    int getMovement();

    int getRange();


    int getHealth();

    int getAttack();
    void setimg(String img);


}
