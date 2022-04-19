package jp.mc.ancientred.starminer.basics.dimention;

import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class MapFromSky
{
  public static boolean hasSkyMapImageData = false;
  public static MapData mapDataFromSky;
  public static byte[] skyMapclientData;
  public static boolean doRecompileSkyMapList = false;
  
  public static void createMapDataFromSky(World world) {
    mapDataFromSky = new MapData("fromskymap");
    createMapFromBiomeCache(world, mapDataFromSky);
  }
  
  public static void createMapFromBiomeCache(World par1World, MapData par3MapData) {
    // Byte code:
    //   0: sipush #128
    //   3: istore_2
    //   4: sipush #128
    //   7: istore_3
    //   8: sipush #128
    //   11: istore #4
    //   13: iconst_0
    //   14: istore #5
    //   16: iconst_0
    //   17: istore #6
    //   19: dconst_0
    //   20: iload #5
    //   22: i2d
    //   23: dsub
    //   24: invokestatic floor_double : (D)I
    //   27: iload #4
    //   29: idiv
    //   30: iload_2
    //   31: iconst_2
    //   32: idiv
    //   33: iadd
    //   34: istore #7
    //   36: dconst_0
    //   37: iload #6
    //   39: i2d
    //   40: dsub
    //   41: invokestatic floor_double : (D)I
    //   44: iload #4
    //   46: idiv
    //   47: iload_3
    //   48: iconst_2
    //   49: idiv
    //   50: iadd
    //   51: istore #8
    //   53: sipush #128
    //   56: iload #4
    //   58: idiv
    //   59: istore #9
    //   61: aload_0
    //   62: getfield provider : Lnet/minecraft/world/WorldProvider;
    //   65: getfield hasNoSky : Z
    //   68: ifeq -> 77
    //   71: iload #9
    //   73: iconst_2
    //   74: idiv
    //   75: istore #9
    //   77: iload #7
    //   79: iload #9
    //   81: isub
    //   82: iconst_1
    //   83: isub
    //   84: bipush #62
    //   86: isub
    //   87: istore #10
    //   89: iload #10
    //   91: iload #7
    //   93: iload #9
    //   95: iadd
    //   96: bipush #63
    //   98: iadd
    //   99: if_icmpge -> 895
    //   102: sipush #255
    //   105: istore #11
    //   107: iconst_0
    //   108: istore #12
    //   110: dconst_0
    //   111: dstore #13
    //   113: iload #8
    //   115: iload #9
    //   117: isub
    //   118: iconst_1
    //   119: isub
    //   120: bipush #62
    //   122: isub
    //   123: istore #15
    //   125: iload #15
    //   127: iload #8
    //   129: iload #9
    //   131: iadd
    //   132: bipush #63
    //   134: iadd
    //   135: if_icmpge -> 889
    //   138: iload #10
    //   140: iload #7
    //   142: isub
    //   143: istore #16
    //   145: iload #15
    //   147: iload #8
    //   149: isub
    //   150: istore #17
    //   152: iload #16
    //   154: iload #16
    //   156: imul
    //   157: iload #17
    //   159: iload #17
    //   161: imul
    //   162: iadd
    //   163: iload #9
    //   165: iconst_2
    //   166: isub
    //   167: iload #9
    //   169: iconst_2
    //   170: isub
    //   171: imul
    //   172: if_icmple -> 179
    //   175: iconst_1
    //   176: goto -> 180
    //   179: iconst_0
    //   180: istore #18
    //   182: iload #5
    //   184: iload #4
    //   186: idiv
    //   187: iload #10
    //   189: iadd
    //   190: iload_2
    //   191: iconst_2
    //   192: idiv
    //   193: isub
    //   194: iload #4
    //   196: imul
    //   197: istore #19
    //   199: iload #6
    //   201: iload #4
    //   203: idiv
    //   204: iload #15
    //   206: iadd
    //   207: iload_3
    //   208: iconst_2
    //   209: idiv
    //   210: isub
    //   211: iload #4
    //   213: imul
    //   214: istore #20
    //   216: aload_0
    //   217: getfield provider : Lnet/minecraft/world/WorldProvider;
    //   220: getfield worldChunkMgr : Lnet/minecraft/world/biome/WorldChunkManager;
    //   223: iload #19
    //   225: iload #20
    //   227: invokevirtual getBiomeGenAt : (II)Lnet/minecraft/world/biome/BiomeGenBase;
    //   230: astore #21
    //   232: getstatic net/minecraft/block/material/MapColor.waterColor : Lnet/minecraft/block/material/MapColor;
    //   235: getfield colorIndex : I
    //   238: istore #22
    //   240: aload #21
    //   242: getstatic net/minecraft/world/biome/BiomeGenBase.beach : Lnet/minecraft/world/biome/BiomeGenBase;
    //   245: if_acmpne -> 256
    //   248: getstatic net/minecraft/block/material/MapColor.sandColor : Lnet/minecraft/block/material/MapColor;
    //   251: getfield colorIndex : I
    //   254: istore #22
    //   256: aload #21
    //   258: getstatic net/minecraft/world/biome/BiomeGenBase.stoneBeach : Lnet/minecraft/world/biome/BiomeGenBase;
    //   261: if_acmpne -> 272
    //   264: getstatic net/minecraft/block/material/MapColor.stoneColor : Lnet/minecraft/block/material/MapColor;
    //   267: getfield colorIndex : I
    //   270: istore #22
    //   272: aload #21
    //   274: getstatic net/minecraft/world/biome/BiomeGenBase.desert : Lnet/minecraft/world/biome/BiomeGenBase;
    //   277: if_acmpne -> 288
    //   280: getstatic net/minecraft/block/material/MapColor.sandColor : Lnet/minecraft/block/material/MapColor;
    //   283: getfield colorIndex : I
    //   286: istore #22
    //   288: aload #21
    //   290: getstatic net/minecraft/world/biome/BiomeGenBase.desertHills : Lnet/minecraft/world/biome/BiomeGenBase;
    //   293: if_acmpne -> 304
    //   296: getstatic net/minecraft/block/material/MapColor.sandColor : Lnet/minecraft/block/material/MapColor;
    //   299: getfield colorIndex : I
    //   302: istore #22
    //   304: aload #21
    //   306: getstatic net/minecraft/world/biome/BiomeGenBase.extremeHills : Lnet/minecraft/world/biome/BiomeGenBase;
    //   309: if_acmpne -> 320
    //   312: getstatic net/minecraft/block/material/MapColor.stoneColor : Lnet/minecraft/block/material/MapColor;
    //   315: getfield colorIndex : I
    //   318: istore #22
    //   320: aload #21
    //   322: getstatic net/minecraft/world/biome/BiomeGenBase.extremeHillsEdge : Lnet/minecraft/world/biome/BiomeGenBase;
    //   325: if_acmpne -> 336
    //   328: getstatic net/minecraft/block/material/MapColor.stoneColor : Lnet/minecraft/block/material/MapColor;
    //   331: getfield colorIndex : I
    //   334: istore #22
    //   336: aload #21
    //   338: getstatic net/minecraft/world/biome/BiomeGenBase.extremeHillsPlus : Lnet/minecraft/world/biome/BiomeGenBase;
    //   341: if_acmpne -> 352
    //   344: getstatic net/minecraft/block/material/MapColor.stoneColor : Lnet/minecraft/block/material/MapColor;
    //   347: getfield colorIndex : I
    //   350: istore #22
    //   352: aload #21
    //   354: getstatic net/minecraft/world/biome/BiomeGenBase.forest : Lnet/minecraft/world/biome/BiomeGenBase;
    //   357: if_acmpne -> 368
    //   360: getstatic net/minecraft/block/material/MapColor.woodColor : Lnet/minecraft/block/material/MapColor;
    //   363: getfield colorIndex : I
    //   366: istore #22
    //   368: aload #21
    //   370: getstatic net/minecraft/world/biome/BiomeGenBase.forestHills : Lnet/minecraft/world/biome/BiomeGenBase;
    //   373: if_acmpne -> 384
    //   376: getstatic net/minecraft/block/material/MapColor.woodColor : Lnet/minecraft/block/material/MapColor;
    //   379: getfield colorIndex : I
    //   382: istore #22
    //   384: aload #21
    //   386: getstatic net/minecraft/world/biome/BiomeGenBase.frozenOcean : Lnet/minecraft/world/biome/BiomeGenBase;
    //   389: if_acmpne -> 400
    //   392: getstatic net/minecraft/block/material/MapColor.iceColor : Lnet/minecraft/block/material/MapColor;
    //   395: getfield colorIndex : I
    //   398: istore #22
    //   400: aload #21
    //   402: getstatic net/minecraft/world/biome/BiomeGenBase.frozenRiver : Lnet/minecraft/world/biome/BiomeGenBase;
    //   405: if_acmpne -> 416
    //   408: getstatic net/minecraft/block/material/MapColor.iceColor : Lnet/minecraft/block/material/MapColor;
    //   411: getfield colorIndex : I
    //   414: istore #22
    //   416: aload #21
    //   418: getstatic net/minecraft/world/biome/BiomeGenBase.iceMountains : Lnet/minecraft/world/biome/BiomeGenBase;
    //   421: if_acmpne -> 432
    //   424: getstatic net/minecraft/block/material/MapColor.snowColor : Lnet/minecraft/block/material/MapColor;
    //   427: getfield colorIndex : I
    //   430: istore #22
    //   432: aload #21
    //   434: getstatic net/minecraft/world/biome/BiomeGenBase.icePlains : Lnet/minecraft/world/biome/BiomeGenBase;
    //   437: if_acmpne -> 448
    //   440: getstatic net/minecraft/block/material/MapColor.snowColor : Lnet/minecraft/block/material/MapColor;
    //   443: getfield colorIndex : I
    //   446: istore #22
    //   448: aload #21
    //   450: getstatic net/minecraft/world/biome/BiomeGenBase.jungle : Lnet/minecraft/world/biome/BiomeGenBase;
    //   453: if_acmpne -> 464
    //   456: getstatic net/minecraft/block/material/MapColor.foliageColor : Lnet/minecraft/block/material/MapColor;
    //   459: getfield colorIndex : I
    //   462: istore #22
    //   464: aload #21
    //   466: getstatic net/minecraft/world/biome/BiomeGenBase.jungleHills : Lnet/minecraft/world/biome/BiomeGenBase;
    //   469: if_acmpne -> 480
    //   472: getstatic net/minecraft/block/material/MapColor.foliageColor : Lnet/minecraft/block/material/MapColor;
    //   475: getfield colorIndex : I
    //   478: istore #22
    //   480: aload #21
    //   482: getstatic net/minecraft/world/biome/BiomeGenBase.jungleEdge : Lnet/minecraft/world/biome/BiomeGenBase;
    //   485: if_acmpne -> 496
    //   488: getstatic net/minecraft/block/material/MapColor.foliageColor : Lnet/minecraft/block/material/MapColor;
    //   491: getfield colorIndex : I
    //   494: istore #22
    //   496: aload #21
    //   498: getstatic net/minecraft/world/biome/BiomeGenBase.mushroomIsland : Lnet/minecraft/world/biome/BiomeGenBase;
    //   501: if_acmpne -> 512
    //   504: getstatic net/minecraft/block/material/MapColor.dirtColor : Lnet/minecraft/block/material/MapColor;
    //   507: getfield colorIndex : I
    //   510: istore #22
    //   512: aload #21
    //   514: getstatic net/minecraft/world/biome/BiomeGenBase.mushroomIslandShore : Lnet/minecraft/world/biome/BiomeGenBase;
    //   517: if_acmpne -> 528
    //   520: getstatic net/minecraft/block/material/MapColor.dirtColor : Lnet/minecraft/block/material/MapColor;
    //   523: getfield colorIndex : I
    //   526: istore #22
    //   528: aload #21
    //   530: getstatic net/minecraft/world/biome/BiomeGenBase.coldBeach : Lnet/minecraft/world/biome/BiomeGenBase;
    //   533: if_acmpne -> 544
    //   536: getstatic net/minecraft/block/material/MapColor.snowColor : Lnet/minecraft/block/material/MapColor;
    //   539: getfield colorIndex : I
    //   542: istore #22
    //   544: aload #21
    //   546: getstatic net/minecraft/world/biome/BiomeGenBase.coldTaiga : Lnet/minecraft/world/biome/BiomeGenBase;
    //   549: if_acmpne -> 560
    //   552: getstatic net/minecraft/block/material/MapColor.snowColor : Lnet/minecraft/block/material/MapColor;
    //   555: getfield colorIndex : I
    //   558: istore #22
    //   560: aload #21
    //   562: getstatic net/minecraft/world/biome/BiomeGenBase.coldTaigaHills : Lnet/minecraft/world/biome/BiomeGenBase;
    //   565: if_acmpne -> 576
    //   568: getstatic net/minecraft/block/material/MapColor.snowColor : Lnet/minecraft/block/material/MapColor;
    //   571: getfield colorIndex : I
    //   574: istore #22
    //   576: aload #21
    //   578: getstatic net/minecraft/world/biome/BiomeGenBase.megaTaiga : Lnet/minecraft/world/biome/BiomeGenBase;
    //   581: if_acmpne -> 592
    //   584: getstatic net/minecraft/block/material/MapColor.snowColor : Lnet/minecraft/block/material/MapColor;
    //   587: getfield colorIndex : I
    //   590: istore #22
    //   592: aload #21
    //   594: getstatic net/minecraft/world/biome/BiomeGenBase.megaTaigaHills : Lnet/minecraft/world/biome/BiomeGenBase;
    //   597: if_acmpne -> 608
    //   600: getstatic net/minecraft/block/material/MapColor.snowColor : Lnet/minecraft/block/material/MapColor;
    //   603: getfield colorIndex : I
    //   606: istore #22
    //   608: aload #21
    //   610: getstatic net/minecraft/world/biome/BiomeGenBase.birchForest : Lnet/minecraft/world/biome/BiomeGenBase;
    //   613: if_acmpne -> 624
    //   616: getstatic net/minecraft/block/material/MapColor.woodColor : Lnet/minecraft/block/material/MapColor;
    //   619: getfield colorIndex : I
    //   622: istore #22
    //   624: aload #21
    //   626: getstatic net/minecraft/world/biome/BiomeGenBase.birchForestHills : Lnet/minecraft/world/biome/BiomeGenBase;
    //   629: if_acmpne -> 640
    //   632: getstatic net/minecraft/block/material/MapColor.woodColor : Lnet/minecraft/block/material/MapColor;
    //   635: getfield colorIndex : I
    //   638: istore #22
    //   640: aload #21
    //   642: getstatic net/minecraft/world/biome/BiomeGenBase.roofedForest : Lnet/minecraft/world/biome/BiomeGenBase;
    //   645: if_acmpne -> 656
    //   648: getstatic net/minecraft/block/material/MapColor.woodColor : Lnet/minecraft/block/material/MapColor;
    //   651: getfield colorIndex : I
    //   654: istore #22
    //   656: aload #21
    //   658: getstatic net/minecraft/world/biome/BiomeGenBase.savanna : Lnet/minecraft/world/biome/BiomeGenBase;
    //   661: if_acmpne -> 672
    //   664: getstatic net/minecraft/block/material/MapColor.grassColor : Lnet/minecraft/block/material/MapColor;
    //   667: getfield colorIndex : I
    //   670: istore #22
    //   672: aload #21
    //   674: getstatic net/minecraft/world/biome/BiomeGenBase.savannaPlateau : Lnet/minecraft/world/biome/BiomeGenBase;
    //   677: if_acmpne -> 688
    //   680: getstatic net/minecraft/block/material/MapColor.grassColor : Lnet/minecraft/block/material/MapColor;
    //   683: getfield colorIndex : I
    //   686: istore #22
    //   688: aload #21
    //   690: getstatic net/minecraft/world/biome/BiomeGenBase.mesa : Lnet/minecraft/world/biome/BiomeGenBase;
    //   693: if_acmpne -> 704
    //   696: getstatic net/minecraft/block/material/MapColor.tntColor : Lnet/minecraft/block/material/MapColor;
    //   699: getfield colorIndex : I
    //   702: istore #22
    //   704: aload #21
    //   706: getstatic net/minecraft/world/biome/BiomeGenBase.mesaPlateau : Lnet/minecraft/world/biome/BiomeGenBase;
    //   709: if_acmpne -> 720
    //   712: getstatic net/minecraft/block/material/MapColor.tntColor : Lnet/minecraft/block/material/MapColor;
    //   715: getfield colorIndex : I
    //   718: istore #22
    //   720: aload #21
    //   722: getstatic net/minecraft/world/biome/BiomeGenBase.mesaPlateau_F : Lnet/minecraft/world/biome/BiomeGenBase;
    //   725: if_acmpne -> 736
    //   728: getstatic net/minecraft/block/material/MapColor.tntColor : Lnet/minecraft/block/material/MapColor;
    //   731: getfield colorIndex : I
    //   734: istore #22
    //   736: aload #21
    //   738: getstatic net/minecraft/world/biome/BiomeGenBase.ocean : Lnet/minecraft/world/biome/BiomeGenBase;
    //   741: if_acmpne -> 752
    //   744: getstatic net/minecraft/block/material/MapColor.waterColor : Lnet/minecraft/block/material/MapColor;
    //   747: getfield colorIndex : I
    //   750: istore #22
    //   752: aload #21
    //   754: getstatic net/minecraft/world/biome/BiomeGenBase.deepOcean : Lnet/minecraft/world/biome/BiomeGenBase;
    //   757: if_acmpne -> 768
    //   760: getstatic net/minecraft/block/material/MapColor.waterColor : Lnet/minecraft/block/material/MapColor;
    //   763: getfield colorIndex : I
    //   766: istore #22
    //   768: aload #21
    //   770: getstatic net/minecraft/world/biome/BiomeGenBase.plains : Lnet/minecraft/world/biome/BiomeGenBase;
    //   773: if_acmpne -> 784
    //   776: getstatic net/minecraft/block/material/MapColor.grassColor : Lnet/minecraft/block/material/MapColor;
    //   779: getfield colorIndex : I
    //   782: istore #22
    //   784: aload #21
    //   786: getstatic net/minecraft/world/biome/BiomeGenBase.river : Lnet/minecraft/world/biome/BiomeGenBase;
    //   789: if_acmpne -> 800
    //   792: getstatic net/minecraft/block/material/MapColor.waterColor : Lnet/minecraft/block/material/MapColor;
    //   795: getfield colorIndex : I
    //   798: istore #22
    //   800: aload #21
    //   802: getstatic net/minecraft/world/biome/BiomeGenBase.hell : Lnet/minecraft/world/biome/BiomeGenBase;
    //   805: if_acmpne -> 808
    //   808: aload #21
    //   810: getstatic net/minecraft/world/biome/BiomeGenBase.sky : Lnet/minecraft/world/biome/BiomeGenBase;
    //   813: if_acmpne -> 816
    //   816: aload #21
    //   818: getstatic net/minecraft/world/biome/BiomeGenBase.swampland : Lnet/minecraft/world/biome/BiomeGenBase;
    //   821: if_acmpne -> 832
    //   824: getstatic net/minecraft/block/material/MapColor.clayColor : Lnet/minecraft/block/material/MapColor;
    //   827: getfield colorIndex : I
    //   830: istore #22
    //   832: aload #21
    //   834: getstatic net/minecraft/world/biome/BiomeGenBase.taiga : Lnet/minecraft/world/biome/BiomeGenBase;
    //   837: if_acmpne -> 848
    //   840: getstatic net/minecraft/block/material/MapColor.snowColor : Lnet/minecraft/block/material/MapColor;
    //   843: getfield colorIndex : I
    //   846: istore #22
    //   848: aload #21
    //   850: getstatic net/minecraft/world/biome/BiomeGenBase.taigaHills : Lnet/minecraft/world/biome/BiomeGenBase;
    //   853: if_acmpne -> 864
    //   856: getstatic net/minecraft/block/material/MapColor.snowColor : Lnet/minecraft/block/material/MapColor;
    //   859: getfield colorIndex : I
    //   862: istore #22
    //   864: aload_1
    //   865: getfield colors : [B
    //   868: iload #10
    //   870: iload #15
    //   872: iload_2
    //   873: imul
    //   874: iadd
    //   875: iload #22
    //   877: iconst_4
    //   878: imul
    //   879: iconst_1
    //   880: iadd
    //   881: i2b
    //   882: bastore
    //   883: iinc #15, 1
    //   886: goto -> 125
    //   889: iinc #10, 1
    //   892: goto -> 89
    //   895: return
    // Line number table:
    //   Java source line number -> byte code offset
    //   #22	-> 0
    //   #23	-> 4
    //   #24	-> 8
    //   #25	-> 13
    //   #26	-> 16
    //   #27	-> 19
    //   #28	-> 36
    //   #29	-> 53
    //   #31	-> 61
    //   #33	-> 71
    //   #36	-> 77
    //   #38	-> 102
    //   #39	-> 107
    //   #40	-> 110
    //   #42	-> 113
    //   #44	-> 138
    //   #45	-> 145
    //   #46	-> 152
    //   #47	-> 182
    //   #48	-> 199
    //   #50	-> 216
    //   #52	-> 232
    //   #53	-> 240
    //   #54	-> 248
    //   #56	-> 256
    //   #57	-> 264
    //   #59	-> 272
    //   #60	-> 280
    //   #62	-> 288
    //   #63	-> 296
    //   #65	-> 304
    //   #66	-> 312
    //   #68	-> 320
    //   #69	-> 328
    //   #71	-> 336
    //   #72	-> 344
    //   #74	-> 352
    //   #75	-> 360
    //   #77	-> 368
    //   #78	-> 376
    //   #80	-> 384
    //   #81	-> 392
    //   #83	-> 400
    //   #84	-> 408
    //   #86	-> 416
    //   #87	-> 424
    //   #89	-> 432
    //   #90	-> 440
    //   #92	-> 448
    //   #93	-> 456
    //   #95	-> 464
    //   #96	-> 472
    //   #98	-> 480
    //   #99	-> 488
    //   #101	-> 496
    //   #102	-> 504
    //   #104	-> 512
    //   #105	-> 520
    //   #108	-> 528
    //   #109	-> 536
    //   #111	-> 544
    //   #112	-> 552
    //   #114	-> 560
    //   #115	-> 568
    //   #117	-> 576
    //   #118	-> 584
    //   #120	-> 592
    //   #121	-> 600
    //   #124	-> 608
    //   #125	-> 616
    //   #127	-> 624
    //   #128	-> 632
    //   #130	-> 640
    //   #131	-> 648
    //   #134	-> 656
    //   #135	-> 664
    //   #137	-> 672
    //   #138	-> 680
    //   #140	-> 688
    //   #141	-> 696
    //   #143	-> 704
    //   #144	-> 712
    //   #146	-> 720
    //   #147	-> 728
    //   #150	-> 736
    //   #151	-> 744
    //   #153	-> 752
    //   #154	-> 760
    //   #156	-> 768
    //   #157	-> 776
    //   #159	-> 784
    //   #160	-> 792
    //   #162	-> 800
    //   #163	-> 808
    //   #164	-> 816
    //   #165	-> 824
    //   #167	-> 832
    //   #168	-> 840
    //   #170	-> 848
    //   #171	-> 856
    //   #175	-> 864
    //   #42	-> 883
    //   #36	-> 889
    //   #184	-> 895
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   145	738	16	k2	I
    //   152	731	17	l2	I
    //   182	701	18	flag	Z
    //   199	684	19	i3	I
    //   216	667	20	j3	I
    //   232	651	21	base	Lnet/minecraft/world/biome/BiomeGenBase;
    //   240	643	22	col	I
    //   125	764	15	j2	I
    //   107	782	11	l1	I
    //   110	779	12	i2	I
    //   113	776	13	d0	D
    //   89	806	10	k1	I
    //   0	896	0	par1World	Lnet/minecraft/world/World;
    //   0	896	1	par3MapData	Lnet/minecraft/world/storage/MapData;
    //   4	892	2	short1	S
    //   8	888	3	short2	S
    //   13	883	4	i	I
    //   16	880	5	j	I
    //   19	877	6	k	I
    //   36	860	7	l	I
    //   53	843	8	i1	I
    //   61	835	9	j1	I
  }
}
