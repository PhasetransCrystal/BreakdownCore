package com.phasetranscrystal.breacore.data.materials.material;

import com.phasetranscrystal.brealib.utils.BreaUtil;

import com.phasetranscrystal.breacore.api.fluid.FluidRegisterBuilder;
import com.phasetranscrystal.breacore.api.fluid.FluidState;
import com.phasetranscrystal.breacore.api.fluid.attribute.FluidAttributes;
import com.phasetranscrystal.breacore.api.fluid.store.FluidStorageKeys;
import com.phasetranscrystal.breacore.api.material.property.PropertyKey;
import com.phasetranscrystal.breacore.api.material.registry.MaterialBuilder;

import static com.phasetranscrystal.breacore.api.material.info.MaterialFlags.*;
import static com.phasetranscrystal.breacore.data.materials.BreaElements.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterialIconSet.*;
import static com.phasetranscrystal.breacore.data.materials.BreaMaterials.*;

public class ElementMaterials {

    public static void register() {
        Actinium = new MaterialBuilder(BreaUtil.byPath("actinium"))
                .color(0xC3D1FF).secondaryColor(0x397090).iconSet(DULL)
                .element(Ac)
                .buildAndRegister();

        Aluminium = new MaterialBuilder(BreaUtil.byPath("aluminium"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(933))
                .ore()
                .color(0x7db9d8).secondaryColor(0x756ac9c)
                .appendFlags(EXT2_METAL, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_RING, GENERATE_FRAME,
                        GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FINE_WIRE)
                .element(Al)
                .blast(1700)
                .buildAndRegister();

        Americium = new MaterialBuilder(BreaUtil.byPath("americium"))
                .ingot(3)
                .liquid(new FluidRegisterBuilder().temperature(1449))
                .plasma()
                .color(0x287869).iconSet(DULL)
                .appendFlags(EXT_METAL, GENERATE_FOIL, GENERATE_FINE_WIRE)
                .element(Am)
                .buildAndRegister();

        Antimony = new MaterialBuilder(BreaUtil.byPath("antimony"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(904))
                .color(0xeaeaff).secondaryColor(0x8181bd).iconSet(DULL)
                .flags(MORTAR_GRINDABLE)
                .element(Sb)
                .buildAndRegister();

        Argon = new MaterialBuilder(BreaUtil.byPath("argon"))
                .gas().plasma()
                .color(0x00FF00)
                .element(Ar)
                .buildAndRegister();

        Arsenic = new MaterialBuilder(BreaUtil.byPath("arsenic"))
                .dust()
                .gas(new FluidRegisterBuilder()
                        .state(FluidState.GAS)
                        .temperature(887))
                .color(0x9c9c8d).secondaryColor(0x676756)
                .element(As)
                .buildAndRegister();

        Astatine = new MaterialBuilder(BreaUtil.byPath("astatine"))
                .color(0x65204f).secondaryColor(0x17212b)
                .element(At)
                .buildAndRegister();

        Barium = new MaterialBuilder(BreaUtil.byPath("barium"))
                .dust()
                .color(0xede192).secondaryColor(0xa7ad4d).iconSet(DULL)
                .element(Ba)
                .buildAndRegister();

        Berkelium = new MaterialBuilder(BreaUtil.byPath("berkelium"))
                .color(0x645A88).iconSet(DULL)
                .element(Bk)
                .buildAndRegister();

        Beryllium = new MaterialBuilder(BreaUtil.byPath("beryllium"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(1560))
                .ore()
                .color(0x73d73d).secondaryColor(0x184537).iconSet(DULL)
                .appendFlags(STD_METAL)
                .element(Be)
                .buildAndRegister();

        Bismuth = new MaterialBuilder(BreaUtil.byPath("bismuth"))
                .ingot(1)
                .liquid(new FluidRegisterBuilder().temperature(545))
                .color(0x5fdddd).secondaryColor(0x517385).iconSet(DULL)
                .element(Bi)
                .buildAndRegister();

        Bohrium = new MaterialBuilder(BreaUtil.byPath("bohrium"))
                .color(0xde67ff).secondaryColor(0xDC57FF).iconSet(DULL)
                .element(Bh)
                .buildAndRegister();

        Boron = new MaterialBuilder(BreaUtil.byPath("boron"))
                .dust()
                .color(0xbffdbf).secondaryColor(0x6d7058)
                .element(B)
                .buildAndRegister();

        Bromine = new MaterialBuilder(BreaUtil.byPath("bromine"))
                .liquid(new FluidRegisterBuilder().attribute(FluidAttributes.ACID))
                .color(0x912200).secondaryColor(0x080101).iconSet(DULL)
                .element(Br)
                .buildAndRegister();

        Caesium = new MaterialBuilder(BreaUtil.byPath("caesium"))
                .dust()
                .color(0xd1821c).secondaryColor(0x231f14).iconSet(DULL)
                .element(Cs)
                .buildAndRegister();

        Calcium = new MaterialBuilder(BreaUtil.byPath("calcium"))
                .dust()
                .color(0xFFF5DE).secondaryColor(0xa4a4a4).iconSet(DULL)
                .element(Ca)
                .buildAndRegister();

        Californium = new MaterialBuilder(BreaUtil.byPath("californium"))
                .color(0xA85A12).iconSet(DULL)
                .element(Cf)
                .buildAndRegister();

        Carbon = new MaterialBuilder(BreaUtil.byPath("carbon"))
                .dust()
                .liquid(new FluidRegisterBuilder().temperature(4600))
                .color(0x333030).secondaryColor(0x221c1c)
                .element(C)
                .buildAndRegister();

        Cadmium = new MaterialBuilder(BreaUtil.byPath("cadmium"))
                .dust()
                .color(0x636377).secondaryColor(0x431a34).iconSet(DULL)
                .element(Cd)
                .buildAndRegister();

        Cerium = new MaterialBuilder(BreaUtil.byPath("cerium"))
                .dust()
                .liquid(new FluidRegisterBuilder().temperature(1068))
                .color(0x87917D).secondaryColor(0x5e6458).iconSet(DULL)
                .element(Ce)
                .buildAndRegister();

        Chlorine = new MaterialBuilder(BreaUtil.byPath("chlorine"))
                .gas(new FluidRegisterBuilder().state(FluidState.GAS).customStill())
                .element(Cl)
                // TODO hazard
                .buildAndRegister();

        Chromium = new MaterialBuilder(BreaUtil.byPath("chromium"))
                .ingot(3)
                .liquid(new FluidRegisterBuilder().temperature(2180))
                .color(0xf3e0ea).secondaryColor(0x441f2e).iconSet(DULL)
                .appendFlags(EXT_METAL, GENERATE_ROTOR)
                .element(Cr)
                .blast(1700)
                .buildAndRegister();

        Cobalt = new MaterialBuilder(BreaUtil.byPath("cobalt"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(1768))
                .ore() // leave for TiCon ore processing
                .color(0x5050FA).secondaryColor(0x2d2d7a).iconSet(DULL)
                .appendFlags(EXT_METAL, GENERATE_FINE_WIRE)
                .element(Co)
                .buildAndRegister();

        Copernicium = new MaterialBuilder(BreaUtil.byPath("copernicium"))
                .color(0x565c5d).secondaryColor(0xffd34b).iconSet(DULL)
                .element(Cn)
                // .radioactiveHazard(1)
                .buildAndRegister();

        Copper = new MaterialBuilder(BreaUtil.byPath("copper"))
                .ingot(1)
                .liquid(new FluidRegisterBuilder().temperature(1358))
                .ore()
                .color(0xe77c56).secondaryColor(0xe4673e).iconSet(DULL)
                .appendFlags(EXT_METAL, MORTAR_GRINDABLE, GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_RING,
                        GENERATE_FINE_WIRE, GENERATE_ROTOR)
                .element(Cu)
                .buildAndRegister();

        Curium = new MaterialBuilder(BreaUtil.byPath("curium"))
                .color(0x7B544E).iconSet(DULL)
                .element(Cm)
                // .radioactiveHazard(1)
                .buildAndRegister();

        Darmstadtium = new MaterialBuilder(BreaUtil.byPath("darmstadtium"))
                .ingot().fluid()
                .color(0x578062).iconSet(DULL)
                .appendFlags(EXT2_METAL, GENERATE_ROTOR, GENERATE_DENSE, GENERATE_SMALL_GEAR)
                .element(Ds)
                .buildAndRegister();

        Deuterium = new MaterialBuilder(BreaUtil.byPath("deuterium"))
                .gas(new FluidRegisterBuilder().state(FluidState.GAS).customStill())
                .element(D)
                .buildAndRegister();

        Dubnium = new MaterialBuilder(BreaUtil.byPath("dubnium"))
                .color(0xc7ddde).secondaryColor(0x00f3ff).iconSet(DULL)
                .element(Db)
                .buildAndRegister();

        Dysprosium = new MaterialBuilder(BreaUtil.byPath("dysprosium"))
                .color(0x6a664b).secondaryColor(0x423307)
                .iconSet(DULL)
                .element(Dy)
                .buildAndRegister();

        Einsteinium = new MaterialBuilder(BreaUtil.byPath("einsteinium"))
                .color(0xCE9F00).iconSet(DULL)
                .element(Es)
                .buildAndRegister();

        Erbium = new MaterialBuilder(BreaUtil.byPath("erbium"))
                .color(0xeccbdb).secondaryColor(0x5d625a)
                .iconSet(DULL)
                .element(Er)
                .buildAndRegister();

        Europium = new MaterialBuilder(BreaUtil.byPath("europium"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(1099))
                .color(0x20FFFF).secondaryColor(0x429393).iconSet(DULL)
                .appendFlags(STD_METAL, GENERATE_LONG_ROD, GENERATE_FINE_WIRE, GENERATE_SPRING, GENERATE_FOIL,
                        GENERATE_FRAME)
                .element(Eu)
                .blast(b -> b.temp(6000))
                .buildAndRegister();

        Fermium = new MaterialBuilder(BreaUtil.byPath("fermium"))
                .color(0xc99fe7).secondaryColor(0x890085).iconSet(DULL)
                .element(Fm)
                // .radioactiveHazard(1)
                .buildAndRegister();

        Flerovium = new MaterialBuilder(BreaUtil.byPath("flerovium"))
                .color(0x2a384e).secondaryColor(0xd2ff00)
                .iconSet(DULL)
                .element(Fl)
                .buildAndRegister();

        Fluorine = new MaterialBuilder(BreaUtil.byPath("fluorine"))
                .gas(new FluidRegisterBuilder().state(FluidState.GAS).customStill())
                .element(F)
                .buildAndRegister();

        Francium = new MaterialBuilder(BreaUtil.byPath("francium"))
                .color(0xAAAAAA).secondaryColor(0x0000ff).iconSet(DULL)
                .element(Fr)
                .buildAndRegister();

        Gadolinium = new MaterialBuilder(BreaUtil.byPath("gadolinium"))
                .color(0x828a7a).secondaryColor(0x363420).iconSet(DULL)
                .element(Gd)
                .buildAndRegister();

        Gallium = new MaterialBuilder(BreaUtil.byPath("gallium"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(303))
                .color(0x7a84ca).secondaryColor(0x13132e).iconSet(DULL)
                .appendFlags(STD_METAL, GENERATE_FOIL)
                .element(Ga)
                .buildAndRegister();

        Germanium = new MaterialBuilder(BreaUtil.byPath("germanium"))
                .color(0x4a4a4a).secondaryColor(0x2d2612).iconSet(DULL)
                .element(Ge)
                .buildAndRegister();

        Gold = new MaterialBuilder(BreaUtil.byPath("gold"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(1337))
                .ore()
                .color(0xfdf55f).secondaryColor(0xf25833).iconSet(DULL)
                .appendFlags(EXT2_METAL, GENERATE_RING, MORTAR_GRINDABLE, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES,
                        GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FINE_WIRE, GENERATE_FOIL)
                .element(Au)
                .buildAndRegister();

        Hafnium = new MaterialBuilder(BreaUtil.byPath("hafnium"))
                .color(0x99999A).secondaryColor(0x2b4a3a).iconSet(DULL)
                .element(Hf)
                .buildAndRegister();

        Hassium = new MaterialBuilder(BreaUtil.byPath("hassium"))
                .color(0x738786).secondaryColor(0x62ffd5)
                .iconSet(DULL)
                .element(Hs)
                .buildAndRegister();

        Holmium = new MaterialBuilder(BreaUtil.byPath("holmium"))
                .color(0xf6fc9c).secondaryColor(0xa3a3a3)
                .iconSet(DULL)
                .element(Ho)
                .buildAndRegister();

        Hydrogen = new MaterialBuilder(BreaUtil.byPath("hydrogen"))
                .gas()
                .color(0x0000B5)
                .element(H)
                .buildAndRegister();

        Helium = new MaterialBuilder(BreaUtil.byPath("helium"))
                .gas(new FluidRegisterBuilder().state(FluidState.GAS).customStill())
                .plasma()
                .liquid(new FluidRegisterBuilder()
                        .temperature(4)
                        .color(0xFCFF90)
                        .name("liquid_helium")
                        .translation("gtceu.fluid.liquid_generic"))
                .element(He)
                .buildAndRegister();
        Helium.getProperty(PropertyKey.FLUID).setPrimaryKey(FluidStorageKeys.GAS);

        Helium3 = new MaterialBuilder(BreaUtil.byPath("helium_3"))
                .gas(new FluidRegisterBuilder()
                        .customStill()
                        .translation("gtceu.fluid.generic"))
                .element(He3)
                .buildAndRegister();

        Indium = new MaterialBuilder(BreaUtil.byPath("indium"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(430))
                .color(0x5c3588).secondaryColor(0x2b0b4a).iconSet(DULL)
                .element(In)
                .buildAndRegister();

        Iodine = new MaterialBuilder(BreaUtil.byPath("iodine"))
                .dust()
                .color(0x3e4467).secondaryColor(0x021e40).iconSet(DULL)
                .element(I)
                .buildAndRegister();

        Iridium = new MaterialBuilder(BreaUtil.byPath("iridium"))
                .ingot(3)
                .liquid(new FluidRegisterBuilder().temperature(2719))
                .color(0x99fede).secondaryColor(0x6cd1cf).iconSet(DULL)
                .appendFlags(EXT2_METAL, GENERATE_FINE_WIRE, GENERATE_GEAR, GENERATE_FRAME)
                .element(Ir)
                .blast(b -> b.temp(4500))
                .buildAndRegister();

        Iron = new MaterialBuilder(BreaUtil.byPath("iron"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(1811))
                .plasma()
                .ore()
                .color(0xeeeeee).secondaryColor(0x979797).iconSet(DULL)
                .appendFlags(EXT2_METAL, MORTAR_GRINDABLE, GENERATE_ROTOR, GENERATE_SMALL_GEAR, GENERATE_GEAR,
                        GENERATE_SPRING_SMALL, GENERATE_SPRING, GENERATE_ROUND, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES,
                        BLAST_FURNACE_CALCITE_TRIPLE)
                .element(Fe)
                .buildAndRegister();

        Krypton = new MaterialBuilder(BreaUtil.byPath("krypton"))
                .gas(new FluidRegisterBuilder()
                        .customStill()
                        .translation("gtceu.fluid.generic"))
                .color(0x80FF80)
                .element(Kr)
                .buildAndRegister();

        Lanthanum = new MaterialBuilder(BreaUtil.byPath("lanthanum"))
                .dust()
                .liquid(new FluidRegisterBuilder().temperature(1193))
                .color(0xd17d50).secondaryColor(0x4a3560).iconSet(DULL)
                .element(La)
                .buildAndRegister();

        Lawrencium = new MaterialBuilder(BreaUtil.byPath("lawrencium"))
                .color(0x5D7575)
                .iconSet(DULL)
                .element(Lr)
                .buildAndRegister();

        Lead = new MaterialBuilder(BreaUtil.byPath("lead"))
                .ingot(1)
                .liquid(new FluidRegisterBuilder().temperature(600))
                .ore()
                .color(0x7e6f82).secondaryColor(0x290633)
                .appendFlags(EXT2_METAL, MORTAR_GRINDABLE, GENERATE_ROTOR, GENERATE_SPRING, GENERATE_SPRING_SMALL,
                        GENERATE_FINE_WIRE)
                .element(Pb)
                .buildAndRegister();

        Lithium = new MaterialBuilder(BreaUtil.byPath("lithium"))
                .dust()
                .liquid(new FluidRegisterBuilder().temperature(454))
                .ore()
                .color(0xd7e7ee).secondaryColor(0xBDC7DB)
                .element(Li)
                .buildAndRegister();

        Livermorium = new MaterialBuilder(BreaUtil.byPath("livermorium"))
                .color(0x939393).secondaryColor(0xff5e5e).iconSet(DULL)
                .element(Lv)
                .buildAndRegister();

        Lutetium = new MaterialBuilder(BreaUtil.byPath("lutetium"))
                .dust()
                .liquid(new FluidRegisterBuilder().temperature(1925))
                .color(0x00ccff).secondaryColor(0x4c687a).iconSet(DULL)
                .element(Lu)
                .buildAndRegister();

        Magnesium = new MaterialBuilder(BreaUtil.byPath("magnesium"))
                .dust()
                .liquid(new FluidRegisterBuilder().temperature(923))
                .color(0xd6e3ff).secondaryColor(0x594d19).iconSet(DULL)
                .element(Mg)
                .buildAndRegister();

        Mendelevium = new MaterialBuilder(BreaUtil.byPath("mendelevium"))
                .color(0x1D4ACF).iconSet(DULL)
                .element(Md)
                .buildAndRegister();

        Manganese = new MaterialBuilder(BreaUtil.byPath("manganese"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(1519))
                .color(0x88a669).secondaryColor(0xCDE1B9)
                .appendFlags(STD_METAL, GENERATE_FOIL, GENERATE_BOLT_SCREW)
                .element(Mn)
                .buildAndRegister();

        Meitnerium = new MaterialBuilder(BreaUtil.byPath("meitnerium"))
                .color(0x4f3c82).secondaryColor(0x6e90ff).iconSet(DULL)
                .element(Mt)
                .buildAndRegister();

        Mercury = new MaterialBuilder(BreaUtil.byPath("mercury"))
                .fluid()
                .color(0xE6DCDC).iconSet(DULL)
                .element(Hg)
                .buildAndRegister();

        Molybdenum = new MaterialBuilder(BreaUtil.byPath("molybdenum"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(2896))
                .ore()
                .color(0xc1c1ce).secondaryColor(0x404068).iconSet(DULL)
                .element(Mo)
                .flags(GENERATE_FOIL, GENERATE_BOLT_SCREW)
                .buildAndRegister();

        Moscovium = new MaterialBuilder(BreaUtil.byPath("moscovium"))
                .color(0x2a1b40).secondaryColor(0xbd91ff).iconSet(DULL)
                .element(Mc)
                .buildAndRegister();

        Neodymium = new MaterialBuilder(BreaUtil.byPath("neodymium"))
                .ingot().fluid().ore()
                .color(0x6c5863).secondaryColor(0x2c1919).iconSet(DULL)
                .appendFlags(STD_METAL, GENERATE_ROD, GENERATE_BOLT_SCREW)
                .element(Nd)
                .blast(1297)
                .buildAndRegister();

        Neon = new MaterialBuilder(BreaUtil.byPath("neon"))
                .gas()
                .color(0xFAB4B4)
                .element(Ne)
                .buildAndRegister();

        Neptunium = new MaterialBuilder(BreaUtil.byPath("neptunium"))
                .color(0x284D7B).iconSet(DULL)
                .element(Np)
                // .radioactiveHazard(1)
                .buildAndRegister();

        Nickel = new MaterialBuilder(BreaUtil.byPath("nickel"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(1728))
                .plasma()
                .ore()
                .color(0xccdff5).secondaryColor(0x59563a).iconSet(DULL)
                .appendFlags(STD_METAL, MORTAR_GRINDABLE)
                .element(Ni)
                .buildAndRegister();

        Nihonium = new MaterialBuilder(BreaUtil.byPath("nihonium"))
                .color(0x323957).secondaryColor(0xbfabff).iconSet(DULL)
                .element(Nh)
                .buildAndRegister();

        Niobium = new MaterialBuilder(BreaUtil.byPath("niobium"))
                .ingot().fluid()
                .color(0xb494b4).secondaryColor(0x4b3f4d).iconSet(DULL)
                .element(Nb)
                .blast(b -> b.temp(2750))
                .buildAndRegister();

        Nitrogen = new MaterialBuilder(BreaUtil.byPath("nitrogen"))
                .gas().plasma()
                .color(0x00BFC1)
                .element(N)
                .buildAndRegister();

        Nobelium = new MaterialBuilder(BreaUtil.byPath("nobelium"))
                .color(0x3e4758).secondaryColor(0x43deff)
                .iconSet(DULL)
                .element(No)
                .buildAndRegister();

        Oganesson = new MaterialBuilder(BreaUtil.byPath("oganesson"))
                .color(0x443936).secondaryColor(0xff1dbd).iconSet(DULL)
                .element(Og)
                .buildAndRegister();

        Osmium = new MaterialBuilder(BreaUtil.byPath("osmium"))
                .ingot(4)
                .liquid(new FluidRegisterBuilder().temperature(3306))
                .color(0x54afff).secondaryColor(0x6e6eff).iconSet(DULL)
                .appendFlags(EXT2_METAL, GENERATE_FOIL)
                .element(Os)
                .blast(b -> b.temp(4500))
                .buildAndRegister();

        Oxygen = new MaterialBuilder(BreaUtil.byPath("oxygen"))
                .gas()
                .liquid(new FluidRegisterBuilder()
                        .temperature(85)
                        .color(0x6688DD)
                        .name("liquid_oxygen")
                        .translation("gtceu.fluid.liquid_generic"))
                .plasma()
                .color(0x4CC3FF)
                .element(O)
                .buildAndRegister();
        Oxygen.getProperty(PropertyKey.FLUID).setPrimaryKey(FluidStorageKeys.GAS);

        Palladium = new MaterialBuilder(BreaUtil.byPath("palladium"))
                .ingot().fluid().ore()
                .color(0xbd92b5).secondaryColor(0x535b14).iconSet(DULL)
                .appendFlags(EXT_METAL, GENERATE_FOIL, GENERATE_FINE_WIRE)
                .element(Pd)
                .blast(b -> b.temp(1828))
                .buildAndRegister();

        Phosphorus = new MaterialBuilder(BreaUtil.byPath("phosphorus"))
                .dust()
                .color(0x77332c).secondaryColor(0x220202)
                .element(P)
                .buildAndRegister();

        Polonium = new MaterialBuilder(BreaUtil.byPath("polonium"))
                .color(0x163b27).secondaryColor(0x00ff78)
                .iconSet(DULL)
                .element(Po)
                // .radioactiveHazard(1)
                .buildAndRegister();

        Platinum = new MaterialBuilder(BreaUtil.byPath("platinum"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(2041))
                .ore()
                .color(0xfff4ba).secondaryColor(0x8d8d71).iconSet(DULL)
                .appendFlags(EXT2_METAL, GENERATE_FOIL, GENERATE_FINE_WIRE, GENERATE_RING, GENERATE_SPRING_SMALL,
                        GENERATE_SPRING)
                .element(Pt)
                .buildAndRegister();

        Plutonium239 = new MaterialBuilder(BreaUtil.byPath("plutonium_239"))
                .ingot(3)
                .liquid(new FluidRegisterBuilder().temperature(913))
                .ore(true)
                .color(0xba2727).secondaryColor(0x222730).iconSet(DULL)
                .element(Pu239)
                .buildAndRegister();

        Plutonium241 = new MaterialBuilder(BreaUtil.byPath("plutonium_241"))
                .ingot(3)
                .liquid(new FluidRegisterBuilder().temperature(913))
                .color(0xff4c4c).secondaryColor(0x222730).iconSet(DULL)
                .appendFlags(EXT_METAL)
                .element(Pu241)
                .buildAndRegister();

        Potassium = new MaterialBuilder(BreaUtil.byPath("potassium"))
                .dust(1)
                .liquid(new FluidRegisterBuilder().temperature(337))
                .color(0xd2e1f2).secondaryColor(0x6189b8).iconSet(DULL)
                .element(K)
                .buildAndRegister();

        Praseodymium = new MaterialBuilder(BreaUtil.byPath("praseodymium"))
                .color(0x718060).secondaryColor(0x3f3447).iconSet(DULL)
                .element(Pr)
                .buildAndRegister();

        Promethium = new MaterialBuilder(BreaUtil.byPath("promethium"))
                .color(0x814947).secondaryColor(0xd0ff71)
                .iconSet(DULL)
                .element(Pm)
                // .radioactiveHazard(1)
                .buildAndRegister();

        Protactinium = new MaterialBuilder(BreaUtil.byPath("protactinium"))
                .color(0xA78B6D).iconSet(DULL)
                .element(Pa)
                // .radioactiveHazard(1)
                .buildAndRegister();

        Radon = new MaterialBuilder(BreaUtil.byPath("radon"))
                .gas()
                .color(0xFF39FF)
                .element(Rn)
                .buildAndRegister();

        Radium = new MaterialBuilder(BreaUtil.byPath("radium"))
                .color(0x838361).secondaryColor(0x89ff21).iconSet(DULL)
                .element(Ra)
                // .radioactiveHazard(1)
                .buildAndRegister();

        Rhenium = new MaterialBuilder(BreaUtil.byPath("rhenium"))
                .color(0xcbcfd7).secondaryColor(0x37393d).iconSet(DULL)
                .element(Re)
                .buildAndRegister();

        Rhodium = new MaterialBuilder(BreaUtil.byPath("rhodium"))
                .ingot().fluid()
                .color(0xfd46b1).secondaryColor(0xDC0C58).iconSet(DULL)
                .appendFlags(EXT2_METAL, GENERATE_GEAR, GENERATE_FINE_WIRE)
                .element(Rh)
                .blast(b -> b.temp(2237))
                .buildAndRegister();

        Roentgenium = new MaterialBuilder(BreaUtil.byPath("roentgenium"))
                .color(0x388c48).secondaryColor(0x198a92).iconSet(DULL)
                .element(Rg)
                .buildAndRegister();

        Rubidium = new MaterialBuilder(BreaUtil.byPath("rubidium"))
                .color(0xde0f0f).secondaryColor(0x3a1f1f).iconSet(DULL)
                .element(Rb)
                .buildAndRegister();

        Ruthenium = new MaterialBuilder(BreaUtil.byPath("ruthenium"))
                .ingot().fluid()
                .color(0xa2cde0).secondaryColor(0x3c7285).iconSet(DULL)
                .flags(GENERATE_FOIL, GENERATE_GEAR)
                .element(Ru)
                .blast(b -> b.temp(2607))
                .buildAndRegister();

        Rutherfordium = new MaterialBuilder(BreaUtil.byPath("rutherfordium"))
                .color(0x6b6157).secondaryColor(0xFFF6A1).iconSet(DULL)
                .element(Rf)
                .buildAndRegister();

        Samarium = new MaterialBuilder(BreaUtil.byPath("samarium"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(1345))
                .color(0xc2c289).secondaryColor(0x235254).iconSet(DULL)
                .flags(GENERATE_LONG_ROD)
                .element(Sm)
                .blast(b -> b.temp(5400))
                .buildAndRegister();

        Scandium = new MaterialBuilder(BreaUtil.byPath("scandium"))
                .color(0xb1b2ac).secondaryColor(0x1c3433)
                .iconSet(DULL)
                .element(Sc)
                .buildAndRegister();

        Seaborgium = new MaterialBuilder(BreaUtil.byPath("seaborgium"))
                .color(0x19C5FF).secondaryColor(0xff19b2).iconSet(DULL)
                .element(Sg)
                .buildAndRegister();

        Selenium = new MaterialBuilder(BreaUtil.byPath("selenium"))
                .color(0xffdf77).secondaryColor(0x055d28).iconSet(DULL)
                .element(Se)
                .buildAndRegister();

        Silicon = new MaterialBuilder(BreaUtil.byPath("silicon"))
                .ingot().fluid()
                .color(0x707078).secondaryColor(0x10293b).iconSet(DULL)
                .flags(GENERATE_FOIL)
                .element(Si)
                .blast(2273) // no gas tier for silicon
                .buildAndRegister();

        Silver = new MaterialBuilder(BreaUtil.byPath("silver"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(1235))
                .ore()
                .color(0xDCDCFF).secondaryColor(0x5a4705).iconSet(DULL)
                .appendFlags(EXT2_METAL, MORTAR_GRINDABLE, GENERATE_FINE_WIRE, GENERATE_RING)
                .element(Ag)
                .buildAndRegister();

        Sodium = new MaterialBuilder(BreaUtil.byPath("sodium"))
                .dust()
                .color(0x7c80ff).secondaryColor(0x2b30a3).iconSet(DULL)
                .element(Na)
                .buildAndRegister();

        Strontium = new MaterialBuilder(BreaUtil.byPath("strontium"))
                .color(0x7a7953).secondaryColor(0x4c0b06).iconSet(DULL)
                .element(Sr)
                .buildAndRegister();

        Sulfur = new MaterialBuilder(BreaUtil.byPath("sulfur"))
                .dust().ore()
                .color(0xfdff31).secondaryColor(0xffb400)
                .flags(FLAMMABLE)
                .element(S)
                .buildAndRegister();

        Tantalum = new MaterialBuilder(BreaUtil.byPath("tantalum"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(3290))
                .color(0xa8a7c6).secondaryColor(0x1f2b20).iconSet(DULL)
                .appendFlags(STD_METAL, GENERATE_FOIL, GENERATE_FINE_WIRE)
                .element(Ta)
                .buildAndRegister();

        Technetium = new MaterialBuilder(BreaUtil.byPath("technetium"))
                .color(0x7430e1).secondaryColor(0x7430e1).iconSet(DULL)
                .element(Tc)
                // .radioactiveHazard(1)
                .buildAndRegister();

        Tellurium = new MaterialBuilder(BreaUtil.byPath("tellurium"))
                .color(0x8fea66).secondaryColor(0x00bfff)
                .iconSet(DULL)
                .element(Te)
                .buildAndRegister();

        Tennessine = new MaterialBuilder(BreaUtil.byPath("tennessine"))
                .color(0x785cc4).secondaryColor(0x7959d4).iconSet(DULL)
                .element(Ts)
                .buildAndRegister();

        Terbium = new MaterialBuilder(BreaUtil.byPath("terbium"))
                .color(0xcedab4).secondaryColor(0x263640)
                .iconSet(DULL)
                .element(Tb)
                .buildAndRegister();

        Thorium = new MaterialBuilder(BreaUtil.byPath("thorium"))
                .ingot()
                .liquid(new FluidRegisterBuilder().temperature(2023))
                .ore()
                .color(0x25411b).secondaryColor(0x051E05).iconSet(DULL)
                .appendFlags(STD_METAL, GENERATE_ROD)
                .element(Th)
                .buildAndRegister();

        Thallium = new MaterialBuilder(BreaUtil.byPath("thallium"))
                .color(0x5d6b8e).secondaryColor(0x815b63).iconSet(DULL)
                .element(Tl)
                // .poison(PoisonProperty.PoisonType.CONTACT)
                .buildAndRegister();

        Thulium = new MaterialBuilder(BreaUtil.byPath("thulium"))
                .color(0x467681).secondaryColor(0x682c2c)
                .iconSet(DULL)
                .element(Tm)
                .buildAndRegister();

        Tin = new MaterialBuilder(BreaUtil.byPath("tin"))
                .ingot(1)
                .liquid(new FluidRegisterBuilder().temperature(505))
                .plasma()
                .ore()
                .color(0xfafeff).secondaryColor(0x4e676c)
                .appendFlags(EXT2_METAL, MORTAR_GRINDABLE, GENERATE_ROTOR, GENERATE_SPRING, GENERATE_SPRING_SMALL,
                        GENERATE_FINE_WIRE)
                .element(Sn)
                .buildAndRegister();

        Titanium = new MaterialBuilder(BreaUtil.byPath("titanium")) // todo Ore? Look at EBF recipe here if we do Ti
                                                                    // ores
                .ingot(3).fluid()
                .color(0xed8eea).secondaryColor(0xff64bc).iconSet(DULL)
                .appendFlags(EXT2_METAL, GENERATE_ROTOR, GENERATE_SMALL_GEAR, GENERATE_GEAR, GENERATE_FRAME)
                .element(Ti)
                .blast(b -> b.temp(1941))
                .buildAndRegister();

        Tritium = new MaterialBuilder(BreaUtil.byPath("tritium"))
                .gas(new FluidRegisterBuilder().state(FluidState.GAS).customStill())
                .color(0xff316b).secondaryColor(0xd00000)
                .iconSet(DULL)
                .element(T)
                .buildAndRegister();

        Tungsten = new MaterialBuilder(BreaUtil.byPath("tungsten"))
                .ingot(3)
                .liquid(new FluidRegisterBuilder().temperature(3695))
                .color(0x3b3a32).secondaryColor(0x2a2800).iconSet(DULL)
                .appendFlags(EXT2_METAL, GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FOIL, GENERATE_GEAR,
                        GENERATE_FRAME)
                .element(W)
                .blast(b -> b.temp(3600))
                .buildAndRegister();

        Uranium238 = new MaterialBuilder(BreaUtil.byPath("uranium_238"))
                .ingot(3)
                .liquid(new FluidRegisterBuilder().temperature(1405))
                .color(0x1d891d).secondaryColor(0x33342c).iconSet(DULL)
                .appendFlags(EXT_METAL)
                .element(U238)
                .buildAndRegister();

        Uranium235 = new MaterialBuilder(BreaUtil.byPath("uranium_235"))
                .ingot(3)
                .liquid(new FluidRegisterBuilder().temperature(1405))
                .color(0x46FA46).secondaryColor(0x33342c).iconSet(DULL)
                .appendFlags(EXT_METAL)
                .element(U235)
                .buildAndRegister();

        Vanadium = new MaterialBuilder(BreaUtil.byPath("vanadium"))
                .ingot().fluid()
                .color(0x696d76).secondaryColor(0x240808).iconSet(DULL)
                .element(V)
                .blast(2183)
                .buildAndRegister();

        Xenon = new MaterialBuilder(BreaUtil.byPath("xenon"))
                .gas()
                .color(0x00FFFF)
                .element(Xe)
                .buildAndRegister();

        Ytterbium = new MaterialBuilder(BreaUtil.byPath("ytterbium"))
                .color(0xA7A7A7).iconSet(DULL)
                .element(Yb)
                .buildAndRegister();

        Yttrium = new MaterialBuilder(BreaUtil.byPath("yttrium"))
                .ingot().fluid()
                .color(0x7d8072).secondaryColor(0x15161a).iconSet(DULL)
                .element(Y)
                .blast(1799)
                .buildAndRegister();

        Zinc = new MaterialBuilder(BreaUtil.byPath("zinc"))
                .ingot(1)
                .liquid(new FluidRegisterBuilder().temperature(693))
                .color(0xEBEBFA).secondaryColor(0x232c30).iconSet(DULL)
                .appendFlags(STD_METAL, MORTAR_GRINDABLE, GENERATE_FOIL, GENERATE_RING, GENERATE_FINE_WIRE)
                .element(Zn)
                .buildAndRegister();

        Zirconium = new MaterialBuilder(BreaUtil.byPath("zirconium"))
                .color(0xb99b7e).secondaryColor(0x271813).iconSet(DULL)
                .element(Zr)
                .buildAndRegister();
    }
}
