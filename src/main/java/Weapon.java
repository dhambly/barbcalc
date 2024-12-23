public class Weapon {
    private String name;
    private final int[] impactZones;
    private final int[] comboModifiers;
    private final int[] comboModifiersAlt;
    private final int[] damageRolls;
    private final boolean isAxe;
    private final boolean isTwoHander;

    public String getName() {
        return name;
    }

    public boolean isAxe() {
        return isAxe;
    }

    public boolean isTwoHander() {
        return isTwoHander;
    }

    public int[] getImpactZones() {
        return impactZones;
    }

    public int[] getComboModifiers() {
        return comboModifiers;
    }

    public int[] getComboModifiersAlt() {
        return comboModifiersAlt;
    }

    public int[] getDamageRolls() {
        return damageRolls;
    }
    //junk poor common uncommon rare epic legendary unique

    public Weapon(int[] impactZones, int[] comboModifiers, int[] comboModifiersAlt, int[] damageRolls, boolean isAxe, boolean isTwoHander) {
        this.impactZones = impactZones;
        this.comboModifiers = comboModifiers;
        this.comboModifiersAlt = comboModifiersAlt;
        this.isAxe = isAxe;
        this.isTwoHander = isTwoHander;
        this.damageRolls = damageRolls;
    }

}
