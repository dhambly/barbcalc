import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CalculatorGUI extends JFrame {
    private JComboBox classBox;
    private JComboBox raceBox;
    private JRadioButton warMaulRadioButton;
    private JTextField baseWeaponDamageBox;
    private JComboBox whereHitBox;
    private JCheckBox axeSpecialistBox;
    private JTextField plusStrengthBox;
    private JTextField plusAllBox;
    private JTextField plusPPBox;
    private JTextField plusPPBBox;
    private JTextField plusWDBox;
    private JTextField addedPDBox;
    private JTextField truePDBox;
    private JTextField armorPenBox;
    private JLabel plusArmorPen;
    private JPanel mainPanel;
    private JTextField enemyArmorBox;
    private JTextField enemyDamageReducBox;
    private JRadioButton fellingAxeButton;
    private JRadioButton vikingButton;
    private JRadioButton fistsButton;
    private JRadioButton franciButton;
    private JRadioButton bardicheWeapon;
    private JRadioButton horsemanWeapon;
    private JRadioButton quarterstaffWeapon;
    private JRadioButton doubleAxeWeapon;
    private JTextPane outputDamage;
    private JCheckBox twoHanderBox;
    private JCheckBox executionerBox;
    private JCheckBox savageBox;
    private JComboBox impactZoneBox;
    private JTextPane characterSheetPane;
    private JRadioButton junkRadioButton;
    private JRadioButton poorRadioButton;
    private JRadioButton commonRadioButton;
    private JRadioButton uncommonRadioButton;
    private JRadioButton rareRadioButton;
    private JRadioButton epicRadioButton;
    private JRadioButton legendaryRadioButton;
    private JRadioButton uniqueRadioButton;

    private enum Rarity {
        JUNK,
        POOR,
        COMMON,
        UNCOMMON,
        RARE,
        EPIC,
        LEGENDARY,
        UNIQUE
    }

    private Rarity rarity;

    private Weapon selectedWeapon;
    public Weapon WARMAUL = new Weapon(new int[]{100, 70}, new int[]{100, 115, 115}, new int[]{90, 120}, new int[]{49, 54, 58, 62, 66, 70, 74, 78}, false, true);
    public Weapon FELLING = new Weapon(new int[]{100, 60}, new int[]{100}, new int[]{}, new int[]{38, 42, 45, 48, 51, 54, 57, 60}, true, true);
    public Weapon VIKING = new Weapon(new int[]{100, 90, 70}, new int[]{100, 105, 110}, new int[]{}, new int[]{28, 31, 33, 35, 37, 39, 41, 43}, false, false);
    public Weapon FISTS = new Weapon(new int[]{100}, new int[]{100, 100}, new int[]{}, new int[]{15, 15, 15, 15, 15, 15, 15, 15}, false, false);
    public Weapon FRANCISCA = new Weapon(new int[]{100}, new int[]{100}, new int[]{}, new int[]{18, 21, 24, 27, 30, 33, 36, 39}, true, false);
    public Weapon BARDICHE = new Weapon(new int[]{100, 60}, new int[]{100, 110, 120}, new int[]{}, new int[]{44, 48, 51, 54, 58, 62, 66, 70}, false, true);
    public Weapon HORSEMAN = new Weapon(new int[]{100, 80}, new int[]{100, 105, 110}, new int[]{}, new int[]{22, 24, 25, 27, 29, 31, 33, 36}, true, false);
    public Weapon QUARTERSTAFF = new Weapon(new int[]{100}, new int[]{100, 110, 110, 110}, new int[]{}, new int[]{28, 31, 33, 35, 37, 39, 42, 45}, false, true);
    public Weapon DOUBLEAXE = new Weapon(new int[]{100, 60}, new int[]{100, 105, 110}, new int[]{110}, new int[]{46, 50, 53, 57, 61, 65, 69, 73}, true, true);

    private void recalculate() {
        ArrayList<Integer> attackChainDamage1 = new ArrayList<>();
        ArrayList<Integer> attackChainDamage2 = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.#");
        try {
            int baseWeaponDam = Integer.parseInt(baseWeaponDamageBox.getText());
            int plusStrength = Integer.parseInt(plusStrengthBox.getText());
            int plusAll = Integer.parseInt(plusAllBox.getText());
            int plusPhysicalPower = Integer.parseInt(plusPPBox.getText());
            double plusPhysicalPowerBonus = Double.parseDouble(plusPPBBox.getText());
            int plusWeaponDamage = Integer.parseInt(plusWDBox.getText());
            int addedDamage = Integer.parseInt(addedPDBox.getText());
            int trueDamage = Integer.parseInt(truePDBox.getText());
            int armorPen = Integer.parseInt(armorPenBox.getText());
            int opponentArmor = Integer.parseInt(enemyArmorBox.getText());
            int opponentDamageReduction = Integer.parseInt(enemyDamageReducBox.getText());

            int strength;
            int physicalPower;
            double physicalPowerBonus = 0;


            strength = calculateBaseStrength();
            strength += plusAll;
            strength += plusStrength;
            physicalPower = strength;
            physicalPower += plusPhysicalPower;
            physicalPowerBonus = calculatePhysicalPowerBonus(physicalPower);
            physicalPowerBonus += plusPhysicalPowerBonus / 100.0;
            double physicalPowerBonusStat = physicalPowerBonus;
            double impactZoneBonus = selectedWeapon.getImpactZones()[impactZoneBox.getSelectedIndex()];
            int[] comboBonusArr = selectedWeapon.getComboModifiers();
            int[] comboBonusArr2 = selectedWeapon.getComboModifiersAlt();
            double hitLocationBonus = getHitLocationBonus();
            double damageReduction = calculatePhysicalDamageReduction(opponentArmor) + opponentDamageReduction / 100.0;

            if (axeSpecialistBox.isSelected()
                    && (selectedWeapon.isAxe())) {
                physicalPowerBonus += 0.1;
            }

            if (savageBox.isSelected()) {
                physicalPowerBonus += 0.1;
            }

            if (twoHanderBox.isSelected()
                    && (selectedWeapon.isTwoHander())) {
                physicalPowerBonus += 0.05;
            }

            //(((((
            // (Base Weapon/Magical Damage + "Buff" Weapon Damage)
            // * Combo Bonus * Impact Zone Bonus)
            // + "Gear" Weapon Damage/Magical Damage + Divine Strike Damage)
            // * (1 + Power Bonus)) + Additional Damage)
            // * (1 + Hit Location Bonus) *
            // (1 - (Damage Reduction * (1 - Penetration)))
            // * (1 - Projectile Reduction)) + True Damage
            for (int comboBonus : comboBonusArr) {
                double damage =
                        (((((baseWeaponDam)
                                * ((comboBonus / 100.0) * (impactZoneBonus / 100.0))
                                + plusWeaponDamage)
                                * (1 + physicalPowerBonus)) + addedDamage)
                                * (hitLocationBonus)
                                * (1 - (damageReduction
                                * (1 - (armorPen / 100.0 + (selectedWeapon == WARMAUL ? 0.3 : 0.0))))))
                                + trueDamage;
                attackChainDamage1.add((int) Math.round(damage));
            }

            for (int comboBonus : comboBonusArr2) {
                double damage =
                        (((((baseWeaponDam)
                                * ((comboBonus / 100.0) * (impactZoneBonus / 100.0))
                                + plusWeaponDamage)
                                * (1 + physicalPowerBonus)) + addedDamage)
                                * (hitLocationBonus)
                                * (1 - (damageReduction
                                * (1 - (armorPen / 100.0)))))
                                + trueDamage;
                attackChainDamage2.add((int) Math.round(damage));
            }


            StringBuilder sb = new StringBuilder();
            sb.append("left click\n");
            for (int i = 0; i < attackChainDamage1.size(); i++) {
                int dam = attackChainDamage1.get(i);
                sb.append("attack ").append(i + 1).append(": ").append(dam).append("\n");
            }
            if (attackChainDamage2.size() > 0) {
                sb.append("right click\n");
                for (int i = 0; i < attackChainDamage2.size(); i++) {
                    int dam = attackChainDamage2.get(i);
                    sb.append("attack ").append(i + 1).append(": ").append(dam).append("\n");
                }
            }
//            sb.append(weapon);
            sb.append("\ntotal enemy damage reduction (no armor pen): ").append(df.format(damageReduction * 100)).append("%\n");
            outputDamage.setText(sb.toString());

            //build character sheet
            sb = new StringBuilder();

            sb.
                    append("Strength: ")
                    .append(strength)
                    .append("\nArmor Penetration: ")
                    .append(armorPen)
                    .append("\nPhysical Power Bonus: ")
                    .append(df.format(physicalPowerBonusStat * 100))
                    .append("\n   From Physical Power: ")
                    .append(physicalPower)
                    .append("\n   From Bonuses: ")
                    .append(plusPhysicalPowerBonus);
            sb.append("\n\nEnemy:")
                    .append("\nPhysical Dam Reduction: ")
                    .append(df.format(damageReduction * 100))
                    .append("\n   From Armor Rating: ")
                    .append(opponentArmor)
                    .append("\n   From Bonuses: ")
                    .append(opponentDamageReduction);

            characterSheetPane.setText(sb.toString());

        } catch (NumberFormatException e) {
            outputDamage.setText("invalid input");
        }
    }

    private double getHitLocationBonus() {
        double hitLocationBonus = 1;
        switch (whereHitBox.getSelectedIndex()) {
            case 0:
                if (executionerBox.isSelected()
                        && (selectedWeapon.isAxe())) {
                    hitLocationBonus = 1.6;
                } else
                    hitLocationBonus = 1.5;
                break;
            case 2:
                hitLocationBonus = 0.5;
                break;
        }
        return hitLocationBonus;
    }

    public static double calculatePhysicalPowerBonus(int physicalPower) {
        double baseBonus = -0.80; // Base bonus for 0 Physical Power

        if (physicalPower >= 0 && physicalPower < 5) {
            return baseBonus + physicalPower * 0.10;
        } else if (physicalPower >= 5 && physicalPower < 7) {
            return -0.30 + (physicalPower - 5) * 0.05;
        } else if (physicalPower >= 7 && physicalPower < 11) {
            return -0.20 + (physicalPower - 7) * 0.03;
        } else if (physicalPower >= 11 && physicalPower < 15) {
            return -0.08 + (physicalPower - 11) * 0.02;
        } else if (physicalPower >= 15 && physicalPower < 50) {
            return 0.0 + (physicalPower - 15) * 0.01;
        } else if (physicalPower >= 50 && physicalPower < 100) {
            return 0.35 + (physicalPower - 50) * 0.005;
        } else if (physicalPower >= 100) {
            return 0.60; // Maximum bonus for physicalPower >= 100
        } else {
            return baseBonus; // Negative bonus for negative physicalPower
        }
    }

    public static double calculatePhysicalDamageReduction(int armorRating) {
        if (armorRating <= -300) {
            return -6.19; // -619%
        } else if (armorRating <= -3) {
            return -0.22 + (armorRating + 300) * 0.02;
        } else if (armorRating <= 22) {
            return -0.25 + (armorRating + 3) * 0.01;
        } else if (armorRating <= 31) {
            return 0.0 + (armorRating - 22) * 0.005;
        } else if (armorRating <= 42) {
            return 0.045 + (armorRating - 31) * 0.004;
        } else if (armorRating <= 52) {
            return 0.089 + (armorRating - 42) * 0.003;
        } else if (armorRating <= 62) {
            return 0.119 + (armorRating - 52) * 0.002;
        } else if (armorRating <= 112) {
            return 0.139 + (armorRating - 62) * 0.001;
        } else if (armorRating <= 175) {
            return 0.189 + (armorRating - 112) * 0.002;
        } else if (armorRating <= 262) {
            return 0.315 + (armorRating - 175) * 0.003;
        } else if (armorRating <= 317) {
            return 0.576 + (armorRating - 262) * 0.002;
        } else if (armorRating <= 400) {
            return 0.686 + (armorRating - 317) * 0.001;
        } else if (armorRating <= 424) {
            return 0.769 + (armorRating - 400) * 0.0005;
        } else if (armorRating <= 450) {
            return 0.781 + (armorRating - 424) * 0.00025;
        } else if (armorRating <= 500) {
            return 0.7975 + (armorRating - 450) * 0.0002;
        } else {
            return 0.7975; // 79.75% (maximum value)
        }
    }

    private int calculateBaseStrength() {
        int baseStrength = 30;
        //race shit
        if (raceBox.getSelectedIndex() == 1) baseStrength++;
        if (raceBox.getSelectedIndex() == 2) baseStrength--;
        return baseStrength;
    }

    private void setImpactZoneBox() {
        ActionListener[] listeners = impactZoneBox.getActionListeners();
        for (ActionListener listener : listeners) {
            impactZoneBox.removeActionListener(listener);
        }
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) impactZoneBox.getModel();
        try {
            model.removeAllElements();
        } catch (Exception ignored) {
        }
        for (int i : selectedWeapon.getImpactZones()) {
            model.addElement(i + "%");
        }
        impactZoneBox.setSelectedIndex(0);
        // Re-add the ActionListener
        for (ActionListener listener : listeners) {
            impactZoneBox.addActionListener(listener);
        }

    }

    private void resetWeaponDamBox() {
        int dam = selectedWeapon.getDamageRolls()[rarity.ordinal()];
        baseWeaponDamageBox.setText(Integer.toString(dam));
    }

    public CalculatorGUI() {
        super();
        selectedWeapon = WARMAUL;
        rarity = Rarity.COMMON;
        resetWeaponDamBox();
        setImpactZoneBox();
        recalculate();
        warMaulRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedWeapon = WARMAUL;
                setImpactZoneBox();
                baseWeaponDamageBox.setEditable(true);
                resetWeaponDamBox();
                recalculate();
            }
        });
        fellingAxeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedWeapon = FELLING;
                setImpactZoneBox();
                baseWeaponDamageBox.setEditable(true);
                resetWeaponDamBox();
                recalculate();
            }
        });
        vikingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedWeapon = VIKING;
                setImpactZoneBox();
                baseWeaponDamageBox.setEditable(true);
                resetWeaponDamBox();
                recalculate();
            }
        });
        fistsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedWeapon = FISTS;
                setImpactZoneBox();
                baseWeaponDamageBox.setEditable(false);
                resetWeaponDamBox();
                recalculate();
            }
        });
        franciButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedWeapon = FRANCISCA;
                setImpactZoneBox();
                baseWeaponDamageBox.setEditable(false);
                resetWeaponDamBox();
                recalculate();
            }
        });
        bardicheWeapon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedWeapon = BARDICHE;
                setImpactZoneBox();
                baseWeaponDamageBox.setEditable(true);
                resetWeaponDamBox();
                recalculate();
            }
        });
        horsemanWeapon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedWeapon = HORSEMAN;
                setImpactZoneBox();
                baseWeaponDamageBox.setEditable(true);
                resetWeaponDamBox();
                recalculate();
            }
        });
        quarterstaffWeapon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedWeapon = QUARTERSTAFF;
                setImpactZoneBox();
                baseWeaponDamageBox.setEditable(true);
                resetWeaponDamBox();
                recalculate();
            }
        });
        doubleAxeWeapon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedWeapon = DOUBLEAXE;
                setImpactZoneBox();
                baseWeaponDamageBox.setEditable(true);
                resetWeaponDamBox();
                recalculate();
            }
        });
        axeSpecialistBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recalculate();
            }
        });
        twoHanderBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recalculate();
            }
        });
        savageBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recalculate();
            }
        });
        executionerBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recalculate();
            }
        });
        raceBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recalculate();
            }
        });
        whereHitBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recalculate();
            }
        });
        impactZoneBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recalculate();
            }
        });
        KeyAdapter listener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                recalculate();
            }
        };
        baseWeaponDamageBox.addKeyListener(listener);
        plusStrengthBox.addKeyListener(listener);
        plusAllBox.addKeyListener(listener);
        plusPPBox.addKeyListener(listener);
        plusPPBBox.addKeyListener(listener);
        plusWDBox.addKeyListener(listener);
        addedPDBox.addKeyListener(listener);
        truePDBox.addKeyListener(listener);
        armorPenBox.addKeyListener(listener);
        enemyArmorBox.addKeyListener(listener);
        enemyDamageReducBox.addKeyListener(listener);
        junkRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rarity = Rarity.JUNK;
                resetWeaponDamBox();
                recalculate();
            }
        });
        poorRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rarity = Rarity.POOR;
                resetWeaponDamBox();
                recalculate();
            }
        });
        commonRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rarity = Rarity.COMMON;
                resetWeaponDamBox();
                recalculate();
            }
        });
        uncommonRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rarity = Rarity.UNCOMMON;
                resetWeaponDamBox();
                recalculate();
            }
        });
        rareRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rarity = Rarity.RARE;
                resetWeaponDamBox();
                recalculate();
            }
        });
        epicRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rarity = Rarity.EPIC;
                resetWeaponDamBox();
                recalculate();
            }
        });
        legendaryRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rarity = Rarity.LEGENDARY;
                resetWeaponDamBox();
                recalculate();
            }
        });
        uniqueRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rarity = Rarity.UNIQUE;
                resetWeaponDamBox();
                recalculate();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CalculatorGUI");
        frame.setContentPane(new CalculatorGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(20, 5, new Insets(0, 0, 0, 0), -1, -1));
        classBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Chad Barbarian");
        defaultComboBoxModel1.addElement("Everyone else sucks");
        classBox.setModel(defaultComboBoxModel1);
        mainPanel.add(classBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(19, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        raceBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("Human");
        defaultComboBoxModel2.addElement("Orc");
        defaultComboBoxModel2.addElement("Elf");
        raceBox.setModel(defaultComboBoxModel2);
        mainPanel.add(raceBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        warMaulRadioButton = new JRadioButton();
        warMaulRadioButton.setSelected(true);
        warMaulRadioButton.setText("War Maul");
        mainPanel.add(warMaulRadioButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fellingAxeButton = new JRadioButton();
        fellingAxeButton.setText("Felling Axe");
        mainPanel.add(fellingAxeButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        vikingButton = new JRadioButton();
        vikingButton.setText("Viking");
        mainPanel.add(vikingButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fistsButton = new JRadioButton();
        fistsButton.setText("Big Barb Fists");
        mainPanel.add(fistsButton, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        franciButton = new JRadioButton();
        franciButton.setText("Franciscas");
        mainPanel.add(franciButton, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bardicheWeapon = new JRadioButton();
        bardicheWeapon.setText("Bardiche");
        mainPanel.add(bardicheWeapon, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        horsemanWeapon = new JRadioButton();
        horsemanWeapon.setText("Horseman");
        mainPanel.add(horsemanWeapon, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        quarterstaffWeapon = new JRadioButton();
        quarterstaffWeapon.setText("Quarterstaff");
        mainPanel.add(quarterstaffWeapon, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        doubleAxeWeapon = new JRadioButton();
        doubleAxeWeapon.setText("Double Axe");
        mainPanel.add(doubleAxeWeapon, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(21, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(0, 2, 20, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Base Weapon Dam");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(20, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        baseWeaponDamageBox = new JTextField();
        baseWeaponDamageBox.setText("15");
        panel1.add(baseWeaponDamageBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("+ Strength");
        panel1.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        plusStrengthBox = new JTextField();
        plusStrengthBox.setText("0");
        panel1.add(plusStrengthBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("+ All");
        panel1.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        plusAllBox = new JTextField();
        plusAllBox.setText("0");
        panel1.add(plusAllBox, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("+ Physical Power");
        panel1.add(label4, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        plusPPBox = new JTextField();
        plusPPBox.setText("0");
        panel1.add(plusPPBox, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("+ Physical Damage %");
        panel1.add(label5, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        plusPPBBox = new JTextField();
        plusPPBBox.setText("0");
        panel1.add(plusPPBBox, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("+ Weapon Damage");
        panel1.add(label6, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        plusWDBox = new JTextField();
        plusWDBox.setText("0");
        panel1.add(plusWDBox, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Additional Physical Damage");
        panel1.add(label7, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addedPDBox = new JTextField();
        addedPDBox.setText("0");
        panel1.add(addedPDBox, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("True Physical Damage");
        panel1.add(label8, new GridConstraints(14, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        truePDBox = new JTextField();
        truePDBox.setText("0");
        panel1.add(truePDBox, new GridConstraints(15, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        plusArmorPen = new JLabel();
        plusArmorPen.setText("+ Armor Pen");
        panel1.add(plusArmorPen, new GridConstraints(16, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        armorPenBox = new JTextField();
        armorPenBox.setText("0");
        panel1.add(armorPenBox, new GridConstraints(17, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        impactZoneBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("100%");
        impactZoneBox.setModel(defaultComboBoxModel3);
        panel1.add(impactZoneBox, new GridConstraints(19, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Impact Zone");
        panel1.add(label9, new GridConstraints(18, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel2, new GridConstraints(0, 4, 20, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        whereHitBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel4 = new DefaultComboBoxModel();
        defaultComboBoxModel4.addElement("Head");
        defaultComboBoxModel4.addElement("Body");
        defaultComboBoxModel4.addElement("Limbs");
        whereHitBox.setModel(defaultComboBoxModel4);
        panel2.add(whereHitBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label10 = new JLabel();
        label10.setText("calculated damage");
        panel3.add(label10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        outputDamage = new JTextPane();
        outputDamage.setEditable(false);
        outputDamage.setText("attack 1");
        panel3.add(outputDamage, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Hit Location");
        panel2.add(label11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(14, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel4, new GridConstraints(2, 0, 8, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        axeSpecialistBox = new JCheckBox();
        axeSpecialistBox.setText("Axe Specialist");
        panel4.add(axeSpecialistBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel4.add(spacer4, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        executionerBox = new JCheckBox();
        executionerBox.setText("Executioner");
        panel4.add(executionerBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        savageBox = new JCheckBox();
        savageBox.setText("Savage (no chest)");
        panel4.add(savageBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        twoHanderBox = new JCheckBox();
        twoHanderBox.setText("Two Hander");
        panel4.add(twoHanderBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        junkRadioButton = new JRadioButton();
        junkRadioButton.setText("Junk");
        panel4.add(junkRadioButton, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        poorRadioButton = new JRadioButton();
        poorRadioButton.setText("Poor");
        panel4.add(poorRadioButton, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        commonRadioButton = new JRadioButton();
        commonRadioButton.setSelected(true);
        commonRadioButton.setText("Common");
        panel4.add(commonRadioButton, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        uncommonRadioButton = new JRadioButton();
        uncommonRadioButton.setText("Uncommon");
        panel4.add(uncommonRadioButton, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rareRadioButton = new JRadioButton();
        rareRadioButton.setText("Rare");
        panel4.add(rareRadioButton, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        epicRadioButton = new JRadioButton();
        epicRadioButton.setText("Epic");
        panel4.add(epicRadioButton, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        legendaryRadioButton = new JRadioButton();
        legendaryRadioButton.setText("Legendary");
        panel4.add(legendaryRadioButton, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        uniqueRadioButton = new JRadioButton();
        uniqueRadioButton.setText("Unique");
        panel4.add(uniqueRadioButton, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Default Weapon Rarity");
        panel4.add(label12, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(7, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel5, new GridConstraints(0, 3, 20, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Target Stats");
        panel5.add(label13, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enemyArmorBox = new JTextField();
        enemyArmorBox.setText("0");
        panel5.add(enemyArmorBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("Armor");
        panel5.add(label14, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enemyDamageReducBox = new JTextField();
        enemyDamageReducBox.setText("0");
        panel5.add(enemyDamageReducBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setText("Phys Damage Reduction");
        panel5.add(label15, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        characterSheetPane = new JTextPane();
        characterSheetPane.setEditable(false);
        characterSheetPane.setText("");
        panel5.add(characterSheetPane, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setText("Expected Character Sheet");
        panel5.add(label16, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(vikingButton);
        buttonGroup.add(vikingButton);
        buttonGroup.add(fellingAxeButton);
        buttonGroup.add(franciButton);
        buttonGroup.add(quarterstaffWeapon);
        buttonGroup.add(doubleAxeWeapon);
        buttonGroup.add(warMaulRadioButton);
        buttonGroup.add(fistsButton);
        buttonGroup.add(bardicheWeapon);
        buttonGroup.add(horsemanWeapon);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(junkRadioButton);
        buttonGroup.add(poorRadioButton);
        buttonGroup.add(commonRadioButton);
        buttonGroup.add(uncommonRadioButton);
        buttonGroup.add(rareRadioButton);
        buttonGroup.add(epicRadioButton);
        buttonGroup.add(legendaryRadioButton);
        buttonGroup.add(uniqueRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
