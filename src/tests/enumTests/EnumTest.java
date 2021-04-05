package tests.enumTests;

import crobes.core.CrobeEnums;

public class EnumTest
{
    public static void main(String[] args) {
        EnumTestClass<CrobeEnums.CrobeColor> tcColor = new EnumTestClass<CrobeEnums.CrobeColor>();

        tcColor.enumeratedValue = CrobeEnums.CrobeColor.aliceblue;
    }
}
