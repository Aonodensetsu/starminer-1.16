package jp.mc.ancientred.starminer.core;

import jp.mc.ancientred.starminer.core.obfuscar.SMCoreObfuscaHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LogWrapper;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class SMTransformer
  implements IClassTransformer
{
  private boolean hasInit = false;
  private boolean hasTweakerLoaded = false;
  public static final String ROTATECORPSEPUBLIC = "rotateCorpsePublic";
  
  public byte[] transform(String name, String transformedName, byte[] bytes) {
    if (!transformedName.startsWith("net.minecraft")) return bytes;

    if (!this.hasInit) {
      checkTweakerLoad();
      this.hasInit = true;
    } 

    if (this.hasTweakerLoaded) return bytes;

    if (transformedName.equals("net.minecraft.world.World")) {
      ClassReader cr = new ClassReader(bytes);
      ClassNode classNode = new ClassNode();
      cr.accept((ClassVisitor)classNode, 0);
      String lablelAsTarget = "World.<init>";
      try {
        String targetMethodName = "<init>";

        for (MethodNode curMnode : classNode.methods) {
          if (targetMethodName.equals(curMnode.name) && curMnode.access == 1)
          {
            for (int i = 0; i < curMnode.instructions.size(); i++) {
              if (curMnode.instructions.get(i).getType() == 5 && ((MethodInsnNode)curMnode.instructions.get(i)).name.equals("<init>") && ((MethodInsnNode)curMnode.instructions.get(i)).owner.equals("java/lang/Object")) {

                LabelNode labelJumpReturn = new LabelNode();
                
                InsnList overrideList = new InsnList();
                overrideList.add((AbstractInsnNode)new VarInsnNode(25, 1));
                overrideList.add((AbstractInsnNode)new JumpInsnNode(199, labelJumpReturn));
                overrideList.add((AbstractInsnNode)new InsnNode(177));
                overrideList.add((AbstractInsnNode)labelJumpReturn);
                
                curMnode.instructions.insert(curMnode.instructions.get(i + 1), overrideList);
                
                LogSucceed(lablelAsTarget);
                break;
              } 
            } 
          }
        } 
      } catch (Exception e) {
        e.printStackTrace();
        return bytes;
      } 
      
      ClassWriter cw = new ClassWriter(1);
      classNode.accept(cw);
      bytes = cw.toByteArray();
    } 

    if (transformedName.equals("net.minecraft.client.renderer.Tessellator")) {
      ClassReader cr = new ClassReader(bytes);
      ClassNode classNode = new ClassNode();
      cr.accept((ClassVisitor)classNode, 0);
      String lablelAsTarget = "Tessellator.instance";
      try {
        String targetMethodName = SMCoreObfuscaHelper.getProperName("instance");
        
        for (FieldNode curFnode : classNode.fields) {
          if (targetMethodName.equals(curFnode.name)) {
            curFnode.access &= 0xFFFFFFEF;
          }
        } 
        
        LogWrapper.log(Level.INFO, "[Starminer]Removed \"Final\" from Tessellator.instance.", new Object[0]);
      } catch (Exception e) {
        e.printStackTrace();
        return bytes;
      } 
      
      ClassWriter cw = new ClassWriter(1);
      classNode.accept(cw);
      bytes = cw.toByteArray();
    } 

    if (transformedName.equals("net.minecraft.item.Item")) {
      ClassReader cr = new ClassReader(bytes);
      ClassNode classNode = new ClassNode();
      cr.accept((ClassVisitor)classNode, 0);

      try {
        String lablelAsTarget = "Item.getMovingObjectPositionFromPlayer";
        String targetMethodName = SMCoreObfuscaHelper.getProperName("getMovingObjectPositionFromPlayer");
        String targetMethoddesc = SMCoreObfuscaHelper.getProperDesc("getMovingObjectPositionFromPlayer");

        for (MethodNode curMnode : classNode.methods) {
          if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {
            LogFound(lablelAsTarget);

            InsnList overrideList = new InsnList();
            overrideList.add((AbstractInsnNode)new VarInsnNode(25, 1));
            overrideList.add((AbstractInsnNode)new VarInsnNode(25, 2));
            overrideList.add((AbstractInsnNode)new VarInsnNode(21, 3));
            overrideList.add((AbstractInsnNode)new MethodInsnNode(184, "jp/mc/ancientred/starminer/core/TransformServerHelper", "getMovingObjectPositionFromPlayerByGravity", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Z)Lnet/minecraft/util/MovingObjectPosition;"));

            overrideList.add((AbstractInsnNode)new InsnNode(176));
            
            curMnode.instructions.insert(curMnode.instructions.get(1), overrideList);
            LogSucceed(lablelAsTarget);
            break;
          } 
        } 
      } catch (Exception e) {
        e.printStackTrace();
        return bytes;
      } 
      
      ClassWriter cw = new ClassWriter(1);
      classNode.accept(cw);
      bytes = cw.toByteArray();
    } 

    if (transformedName.equals("net.minecraft.block.BlockLiquid")) {
      ClassReader cr = new ClassReader(bytes);
      ClassNode classNode = new ClassNode();
      cr.accept((ClassVisitor)classNode, 0);
      String lablelAsTarget = "BlockLiquid.onBlockAdded";
      try {
        String targetMethodName = SMCoreObfuscaHelper.getProperName("onBlockAdded");
        String targetMethoddesc = SMCoreObfuscaHelper.getProperDesc("onBlockAdded");

        for (MethodNode curMnode : classNode.methods) {
          if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {
            LogFound(lablelAsTarget);

            InsnList overrideList = new InsnList();
            overrideList.add((AbstractInsnNode)new VarInsnNode(25, 0));
            overrideList.add((AbstractInsnNode)new VarInsnNode(25, 1));
            overrideList.add((AbstractInsnNode)new VarInsnNode(21, 2));
            overrideList.add((AbstractInsnNode)new VarInsnNode(21, 3));
            overrideList.add((AbstractInsnNode)new VarInsnNode(21, 4));
            overrideList.add((AbstractInsnNode)new MethodInsnNode(184, "jp/mc/ancientred/starminer/core/TransformServerHelper", "blockLiquidOnBlockAddedHook", "(Lnet/minecraft/block/Block;Lnet/minecraft/world/World;III)Z"));

            LabelNode labelJumpReturn = new LabelNode();
            overrideList.add((AbstractInsnNode)new JumpInsnNode(153, labelJumpReturn));
            overrideList.add((AbstractInsnNode)new InsnNode(177));
            overrideList.add((AbstractInsnNode)labelJumpReturn);
            
            curMnode.instructions.insert(curMnode.instructions.get(1), overrideList);
            LogSucceed(lablelAsTarget);
            break;
          } 
        } 
      } catch (Exception e) {
        e.printStackTrace();
        return bytes;
      } 
      
      ClassWriter cw = new ClassWriter(1);
      classNode.accept(cw);
      bytes = cw.toByteArray();
    } 

    if (transformedName.equals("net.minecraft.entity.Entity")) {
      ClassReader cr = new ClassReader(bytes);
      ClassNode classNode = new ClassNode();
      cr.accept((ClassVisitor)classNode, 0);
      
      try {
        String lablelAsTarget = "Entity.onEntityUpdate";
        String targetMethodName = SMCoreObfuscaHelper.getProperName("onEntityUpdate");
        String targetMethoddesc = SMCoreObfuscaHelper.getProperDesc("onEntityUpdate");

        for (MethodNode curMnode : classNode.methods) {
          if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {
            LogFound(lablelAsTarget);
            Double cstCheck = new Double("-64.0");
            
            for (int i = curMnode.instructions.size() - 1; i >= 0; i--) {
              if (curMnode.instructions.get(i).getType() == 9 && ((LdcInsnNode)curMnode.instructions.get(i)).cst.equals(cstCheck)) {

                ((LdcInsnNode)curMnode.instructions.get(i)).cst = new Double("-512D");
                
                LogSucceed(lablelAsTarget);
                break;
              } 
            } 
          } 
        } 
      } catch (Exception e) {
        e.printStackTrace();
        return bytes;
      } 
      
      ClassWriter cw = new ClassWriter(1);
      classNode.accept(cw);
      bytes = cw.toByteArray();
    } 

    if (transformedName.equals("net.minecraft.entity.item.EntityItem")) {
      ClassReader cr = new ClassReader(bytes);
      ClassNode classNode = new ClassNode();
      cr.accept((ClassVisitor)classNode, 0);
      String lablelAsTarget = "EntityItem.onUpdate";
      try {
        String targetMethodName = SMCoreObfuscaHelper.getProperName("onUpdate");
        String targetMethoddesc = SMCoreObfuscaHelper.getProperDesc("onUpdate");

        for (MethodNode curMnode : classNode.methods) {
          if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {
            LogFound(lablelAsTarget);
            
            Double cstCheck = new Double("0.03999999910593033");
            
            for (int i = curMnode.instructions.size() - 1; i >= 0; i--) {
              if (curMnode.instructions.get(i).getType() == 9 && ((LdcInsnNode)curMnode.instructions.get(i)).cst.equals(cstCheck)) {

                InsnList overrideList = new InsnList();
                overrideList.add((AbstractInsnNode)new VarInsnNode(25, 0));
                overrideList.add((AbstractInsnNode)new MethodInsnNode(184, "jp/mc/ancientred/starminer/core/TransformServerHelper", "pullGravityYInGravity", "(Lnet/minecraft/entity/Entity;)D"));

                curMnode.instructions.remove(curMnode.instructions.get(i));
                curMnode.instructions.insert(curMnode.instructions.get(i - 1), overrideList);
                LogSucceed(lablelAsTarget);
                break;
              } 
            } 
          } 
        } 
      } catch (Exception e) {
        e.printStackTrace();
        LogFailed(lablelAsTarget);
        return bytes;
      } 
      
      ClassWriter cw = new ClassWriter(1);
      classNode.accept(cw);
      bytes = cw.toByteArray();
    } 

    if (transformedName.equals("net.minecraft.entity.player.EntityPlayer")) {
      ClassReader cr = new ClassReader(bytes);
      ClassNode classNode = new ClassNode();
      cr.accept((ClassVisitor)classNode, 0);
      
      String lablelAsTarget = "EntityPlayer";
      try {
        for (MethodNode curMnode : classNode.methods) {
          lablelAsTarget = "EntityPlayer.moveEntityWithHeading";
          String targetMethodName = SMCoreObfuscaHelper.getProperName("moveEntityWithHeading");
          String targetMethoddesc = SMCoreObfuscaHelper.getProperDesc("moveEntityWithHeading");
          if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {
            LogFound(lablelAsTarget);
            
            for (int i = curMnode.instructions.size() - 1; i >= 0; i--) {
              if (curMnode.instructions.get(i).getType() == 4 && ((FieldInsnNode)curMnode.instructions.get(i)).getOpcode() == 181 && ((FieldInsnNode)curMnode.instructions.get(i)).owner.equals(SMCoreObfuscaHelper.getProperName("net/minecraft/entity/player/EntityPlayer")) && ((FieldInsnNode)curMnode.instructions.get(i)).name.equals(SMCoreObfuscaHelper.getProperName("motionY")) && curMnode.instructions.get(i - 1).getOpcode() == 107 && curMnode.instructions.get(i - 2).getType() == 9 && curMnode.instructions.get(i - 3).getOpcode() == 24 && curMnode.instructions.get(i - 4).getOpcode() == 25) {

                curMnode.instructions.remove(curMnode.instructions.get(i));
                curMnode.instructions.remove(curMnode.instructions.get(i - 1));
                curMnode.instructions.remove(curMnode.instructions.get(i - 2));
                curMnode.instructions.remove(curMnode.instructions.get(i - 3));
                curMnode.instructions.remove(curMnode.instructions.get(i - 4));
                
                LogSucceed(lablelAsTarget);
                
                break;
              } 
            } 
          } 
          lablelAsTarget = "EntityPlayer.getPosition";
          targetMethodName = SMCoreObfuscaHelper.getProperName("getPosition");
          targetMethoddesc = SMCoreObfuscaHelper.getProperDesc("getPosition");
          if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {
            LogFound(lablelAsTarget);

            InsnList overrideList = new InsnList();
            overrideList.add((AbstractInsnNode)new VarInsnNode(25, 0));
            overrideList.add((AbstractInsnNode)new VarInsnNode(23, 1));
            overrideList.add((AbstractInsnNode)new MethodInsnNode(184, "jp/mc/ancientred/starminer/core/TransformClientHelper", "getPositionForgeHook", "(Lnet/minecraft/entity/player/EntityPlayer;F)Lnet/minecraft/util/Vec3;"));

            overrideList.add((AbstractInsnNode)new InsnNode(176));
            
            curMnode.instructions.insert(curMnode.instructions.get(1), overrideList);
            LogSucceed(lablelAsTarget);
            
            break;
          } 
        } 
      } catch (Exception e) {
        e.printStackTrace();
        LogFailed(lablelAsTarget);
        return bytes;
      } 
      
      ClassWriter cw = new ClassWriter(1);
      classNode.accept(cw);
      bytes = cw.toByteArray();
    } 

    if (transformedName.equals("net.minecraft.client.entity.EntityPlayerSP")) {
      ClassReader cr = new ClassReader(bytes);
      ClassNode classNode = new ClassNode();
      cr.accept((ClassVisitor)classNode, 0);
      String lablelAsTarget = "EntityPlayerSP.onLivingUpdate";
      try {
        String targetMethodName = SMCoreObfuscaHelper.getProperName("onLivingUpdate");
        String targetMethoddesc = SMCoreObfuscaHelper.getProperDesc("onLivingUpdate");

        for (MethodNode curMnode : classNode.methods) {
          if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {
            LogFound(lablelAsTarget);
            
            int i;
            for (i = curMnode.instructions.size() - 1; i >= 0; i--) {
              if (curMnode.instructions.get(i).getOpcode() == 25 && curMnode.instructions.get(i + 1).getType() == 4 && ((FieldInsnNode)curMnode.instructions.get(i + 1)).getOpcode() == 180 && ((FieldInsnNode)curMnode.instructions.get(i + 1)).owner.equals(SMCoreObfuscaHelper.getProperName("net/minecraft/client/entity/EntityPlayerSP")) && ((FieldInsnNode)curMnode.instructions.get(i + 1)).name.equals(SMCoreObfuscaHelper.getProperName("isCollidedHorizontally")) && curMnode.instructions.get(i + 2).getType() == 7 && ((JumpInsnNode)curMnode.instructions.get(i + 2)).getOpcode() == 154) {

                curMnode.instructions.remove(curMnode.instructions.get(i + 2));
                curMnode.instructions.remove(curMnode.instructions.get(i + 1));
                curMnode.instructions.remove(curMnode.instructions.get(i));
                
                LogSucceed(lablelAsTarget + "(A)");
                
                break;
              } 
            } 
            
            for (i = curMnode.instructions.size() - 1; i >= 0; i--) {
              if (curMnode.instructions.get(i).getOpcode() == 25 && curMnode.instructions.get(i + 1).getType() == 5 && ((MethodInsnNode)curMnode.instructions.get(i + 1)).getOpcode() == 182 && ((MethodInsnNode)curMnode.instructions.get(i + 1)).owner.equals(SMCoreObfuscaHelper.getProperName("net/minecraft/client/entity/EntityPlayerSP")) && ((MethodInsnNode)curMnode.instructions.get(i + 1)).name.equals(SMCoreObfuscaHelper.getProperName("isRidingHorse")) && ((MethodInsnNode)curMnode.instructions.get(i + 1)).desc.equals("()Z")) {

                InsnList overrideList = new InsnList();
                overrideList.add((AbstractInsnNode)new VarInsnNode(25, 0));
                overrideList.add((AbstractInsnNode)new MethodInsnNode(184, "jp/mc/ancientred/starminer/core/TransformClientHelper", "setFlyMovementByGravity", "(Lnet/minecraft/entity/player/EntityPlayer;)V"));

                curMnode.instructions.insert(curMnode.instructions.get(i - 1), overrideList);
                LogSucceed(lablelAsTarget + "(B)");
                break;
              } 
            } 
          } 
        } 
      } catch (Exception e) {
        e.printStackTrace();
        LogFailed(lablelAsTarget);
        return bytes;
      } 

      ClassWriter cw = new ClassWriter(1);
      classNode.accept(cw);
      bytes = cw.toByteArray();
    } 

    if (transformedName.equals("net.minecraft.entity.EntityLiving")) {
      ClassReader cr = new ClassReader(bytes);
      ClassNode classNode = new ClassNode();
      cr.accept((ClassVisitor)classNode, 0);
      String lablelAsTarget = "EntityLiving.updateAITasks";
      try {
        String targetMethodName = SMCoreObfuscaHelper.getProperName("updateAITasks");
        String targetMethoddesc = SMCoreObfuscaHelper.getProperDesc("updateAITasks");

        for (MethodNode curMnode : classNode.methods) {
          if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {
            LogFound(lablelAsTarget);

            LabelNode labelJump = new LabelNode();
            InsnList overrideList = new InsnList();
            overrideList.add((AbstractInsnNode)new VarInsnNode(25, 0));
            overrideList.add((AbstractInsnNode)new MethodInsnNode(184, "jp/mc/ancientred/starminer/core/TransformServerHelper", "updateAITasksByGravity", "(Lnet/minecraft/entity/EntityLivingBase;)Z"));

            overrideList.add((AbstractInsnNode)new JumpInsnNode(153, labelJump));
            overrideList.add((AbstractInsnNode)new InsnNode(177));
            overrideList.add((AbstractInsnNode)labelJump);
            
            curMnode.instructions.insert(curMnode.instructions.get(1), overrideList);
            
            LogSucceed(lablelAsTarget);
            break;
          } 
        } 
      } catch (Exception e) {
        e.printStackTrace();
        LogFailed(lablelAsTarget);
        return bytes;
      } 

      ClassWriter cw = new ClassWriter(1);
      classNode.accept(cw);
      bytes = cw.toByteArray();
    } 

    if (transformedName.equals("net.minecraft.client.renderer.EntityRenderer")) {
      ClassReader cr = new ClassReader(bytes);
      ClassNode classNode = new ClassNode();
      cr.accept((ClassVisitor)classNode, 0);
      String lablelAsTarget = "EntityRenderer.orientCamera";
      try {
        String targetMethodName = SMCoreObfuscaHelper.getProperName("orientCamera");
        String targetMethoddesc = SMCoreObfuscaHelper.getProperDesc("orientCamera");

        for (MethodNode curMnode : classNode.methods) {
          if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {
            LogFound(lablelAsTarget);
            for (int i = curMnode.instructions.size() - 1; i >= 0; i--) {
              if (curMnode.instructions.get(i).getType() == 7 && curMnode.instructions.get(i - 1).getType() == 4 && ((FieldInsnNode)curMnode.instructions.get(i - 1)).name.equals(SMCoreObfuscaHelper.getProperName("debugCamEnable")) && curMnode.instructions.get(i - 2).getType() == 4 && ((FieldInsnNode)curMnode.instructions.get(i - 2)).name.equals(SMCoreObfuscaHelper.getProperName("gameSettings")) && curMnode.instructions.get(i - 3).getType() == 4 && ((FieldInsnNode)curMnode.instructions.get(i - 3)).name.equals(SMCoreObfuscaHelper.getProperName("mc"))) {

                InsnList overrideList = new InsnList();
                overrideList.add((AbstractInsnNode)new VarInsnNode(23, 1));
                overrideList.add((AbstractInsnNode)new MethodInsnNode(184, "jp/mc/ancientred/starminer/core/TransformClientHelper", "orientCameraByGravity", "(F)V"));

                overrideList.add((AbstractInsnNode)new InsnNode(4));
                
                curMnode.instructions.insert(curMnode.instructions.get(i - 1), overrideList);
                LogSucceed(lablelAsTarget + "(A)");
                
                break;
              } 
            } 
            boolean firstJumpFound = false;
            LabelNode labelJump = null; int j;
            for (j = 0; j < curMnode.instructions.size(); j++) {
              if (curMnode.instructions.get(j).getType() == 4 && ((FieldInsnNode)curMnode.instructions.get(j)).name.equals(SMCoreObfuscaHelper.getProperName("thirdPersonView")) && ((FieldInsnNode)curMnode.instructions.get(j)).owner.equals(SMCoreObfuscaHelper.getProperName("net/minecraft/client/settings/GameSettings")) && curMnode.instructions.get(j + 1).getType() == 7 && ((JumpInsnNode)curMnode.instructions.get(j + 1)).getOpcode() == 158) {

                firstJumpFound = true;
                labelJump = ((JumpInsnNode)curMnode.instructions.get(j + 1)).label;
                LogSucceed(lablelAsTarget + "(B_1)");
              
              }
              else if (firstJumpFound && curMnode.instructions.get(j).getOpcode() == 106 && curMnode.instructions.get(j + 1).getOpcode() == 98 && curMnode.instructions.get(j + 2).getOpcode() == 141 && curMnode.instructions.get(j + 3).getOpcode() == 57 && curMnode.instructions.get(j + 3).getType() == 2) {

                int d3VarNum = ((VarInsnNode)curMnode.instructions.get(j + 3)).var;

                InsnList overrideList = new InsnList();
                overrideList.add((AbstractInsnNode)new VarInsnNode(24, d3VarNum));
                overrideList.add((AbstractInsnNode)new VarInsnNode(23, 1));
                overrideList.add((AbstractInsnNode)new MethodInsnNode(184, "jp/mc/ancientred/starminer/core/TransformClientHelper", "roatate3rdPersonViewByGravity", "(DF)Z"));

                overrideList.add((AbstractInsnNode)new JumpInsnNode(154, labelJump));
                
                curMnode.instructions.insert(curMnode.instructions.get(j + 3), overrideList);
                
                LogSucceed(lablelAsTarget + "(B_2)");
                
                break;
              } 
            } 
            for (j = 0; j < curMnode.instructions.size(); j++) {
              if (curMnode.instructions.get(j).getOpcode() == 25 && curMnode.instructions.get(j + 1).getType() == 5 && ((MethodInsnNode)curMnode.instructions.get(j + 1)).getOpcode() == 182 && ((MethodInsnNode)curMnode.instructions.get(j + 1)).name.equals(SMCoreObfuscaHelper.getProperName("isPlayerSleeping")) && ((MethodInsnNode)curMnode.instructions.get(j + 1)).desc.equals(SMCoreObfuscaHelper.getProperDesc("isPlayerSleeping")) && curMnode.instructions.get(j + 2).getType() == 7 && curMnode.instructions.get(j + 2).getOpcode() == 153 && curMnode.instructions.get(j + 3).getType() == 8) {

                labelJump = new LabelNode();
                InsnList overrideList = new InsnList();
                overrideList.add((AbstractInsnNode)new VarInsnNode(23, 1));
                overrideList.add((AbstractInsnNode)new MethodInsnNode(184, "jp/mc/ancientred/starminer/core/TransformClientHelper", "rotateSleepingViewByGravity", "(F)Z"));

                overrideList.add((AbstractInsnNode)new JumpInsnNode(154, labelJump));
                overrideList.add((AbstractInsnNode)new InsnNode(177));
                overrideList.add((AbstractInsnNode)labelJump);
                
                curMnode.instructions.insert(curMnode.instructions.get(j + 3), overrideList);
                
                LogSucceed(lablelAsTarget + "(B_3)");
                break;
              } 
            } 
          } 
        } 
        lablelAsTarget = "EntityRenderer.updateFogColor";
        targetMethodName = SMCoreObfuscaHelper.getProperName("updateFogColor");
        targetMethoddesc = SMCoreObfuscaHelper.getProperDesc("updateFogColor");
        
        for (MethodNode curMnode : classNode.methods) {
          if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {
            LogFound(lablelAsTarget);
            
            for (int i = curMnode.instructions.size() - 1; i >= 0; i--) {
              if (curMnode.instructions.get(i).getType() == 2 && curMnode.instructions.get(i).getOpcode() == 24 && curMnode.instructions.get(i + 1).getType() == 2 && curMnode.instructions.get(i + 1).getOpcode() == 24 && ((VarInsnNode)curMnode.instructions.get(i)).var == ((VarInsnNode)curMnode.instructions.get(i + 1)).var && curMnode.instructions.get(i + 2).getOpcode() == 107 && curMnode.instructions.get(i + 3).getOpcode() == 57) {

                for (int j = i - 1; j >= 0; j--) {
                  if (curMnode.instructions.get(j).getOpcode() == 15 && curMnode.instructions.get(j + 1).getOpcode() == 152) {
                    
                    InsnList overrideList = new InsnList();
                    overrideList.add((AbstractInsnNode)new LdcInsnNode(new Double("-999999D")));
                    curMnode.instructions.remove(curMnode.instructions.get(j));
                    curMnode.instructions.insert(curMnode.instructions.get(j - 1), overrideList);
                    LogSucceed(lablelAsTarget);
                    break;
                  } 
                } 
                break;
              } 
            } 
          } 
        } 
      } catch (Exception e) {
        e.printStackTrace();
        LogFailed(lablelAsTarget);
        return bytes;
      } 

      ClassWriter cw = new ClassWriter(1);
      classNode.accept(cw);
      bytes = cw.toByteArray();
    } 

    if (transformedName.equals("net.minecraft.client.renderer.entity.RendererLivingEntity")) {
      ClassReader cr = new ClassReader(bytes);
      ClassNode classNode = new ClassNode();
      cr.accept((ClassVisitor)classNode, 0);
      String lablelAsTarget = "RendererLivingEntity.rotateCorpse";
      try {
        String targetMethodName = SMCoreObfuscaHelper.getProperName("rotateCorpse");
        String targetMethoddesc = SMCoreObfuscaHelper.getProperDesc("rotateCorpse");

        for (MethodNode curMnode : classNode.methods) {
          if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {
            LogFound(lablelAsTarget);

            InsnList overrideList = new InsnList();
            overrideList.add((AbstractInsnNode)new VarInsnNode(25, 1));
            overrideList.add((AbstractInsnNode)new VarInsnNode(23, 2));
            overrideList.add((AbstractInsnNode)new VarInsnNode(23, 3));
            overrideList.add((AbstractInsnNode)new VarInsnNode(23, 4));
            overrideList.add((AbstractInsnNode)new MethodInsnNode(184, "jp/mc/ancientred/starminer/core/TransformClientHelper", "rotateCorpseByGravity", "(Lnet/minecraft/entity/EntityLivingBase;FFF)V"));

            overrideList.add((AbstractInsnNode)new InsnNode(177));
            
            curMnode.instructions.insert(curMnode.instructions.get(1), overrideList);
            LogSucceed(lablelAsTarget);
            
            break;
          } 
        } 
        lablelAsTarget = "RendererLivingEntity.doRender";
        targetMethodName = SMCoreObfuscaHelper.getProperName("doRender");
        targetMethoddesc = SMCoreObfuscaHelper.getProperDesc("doRender");

        for (MethodNode curMnode : classNode.methods) {
          if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {
            LogFound(lablelAsTarget);
            for (int i = 0; i < curMnode.instructions.size(); i++) {
              if (curMnode.instructions.get(i).getType() == 5 && curMnode.instructions.get(i).getOpcode() == 182 && ((MethodInsnNode)curMnode.instructions.get(i)).owner.equals(SMCoreObfuscaHelper.getProperName("net/minecraft/client/renderer/entity/RendererLivingEntity")) && ((MethodInsnNode)curMnode.instructions.get(i)).name.equals(SMCoreObfuscaHelper.getProperName("rotateCorpse")) && ((MethodInsnNode)curMnode.instructions.get(i)).desc.equals(SMCoreObfuscaHelper.getProperDesc("rotateCorpse"))) {

                InsnList overrideList = new InsnList();
                overrideList.add((AbstractInsnNode)new MethodInsnNode(184, "jp/mc/ancientred/starminer/core/TransformClientHelper", "rotateCorpseProxy", "(Lnet/minecraft/client/renderer/entity/RendererLivingEntity;Lnet/minecraft/entity/EntityLivingBase;FFF)V"));

                curMnode.instructions.remove(curMnode.instructions.get(i));
                curMnode.instructions.insert(curMnode.instructions.get(i - 1), overrideList);
                LogSucceed(lablelAsTarget);

                
                break;
              } 
            } 
          } 
        } 
        
        boolean hasRotateCorpsePublic = false;
        for (MethodNode curMnode : classNode.methods) {
          if (curMnode.name.equals("rotateCorpsePublic")) {
            hasRotateCorpsePublic = true;
            break;
          } 
        } 
        if (!hasRotateCorpsePublic) {
          classNode.methods.add(createRotateCorpsePublic());
        }
      } catch (Exception e) {
        e.printStackTrace();
        LogFailed(lablelAsTarget);
        return bytes;
      } 

      ClassWriter cw = new ClassWriter(1);
      classNode.accept(cw);
      bytes = cw.toByteArray();
    } 

    if (transformedName.equals("net.minecraft.network.NetHandlerPlayServer")) {
      ClassReader cr = new ClassReader(bytes);
      ClassNode classNode = new ClassNode();
      cr.accept((ClassVisitor)classNode, 0);
      
      String lablelAsTarget = "NetHandlerPlayServer.processPlayer";
      
      try {
        String targetMethodName = SMCoreObfuscaHelper.getProperName("processPlayer");
        String targetMethoddesc = SMCoreObfuscaHelper.getProperDesc("processPlayer");

        for (MethodNode curMnode : classNode.methods) {
          if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {
            LogFound(lablelAsTarget);

            for (int i = 0; i < curMnode.instructions.size(); i++) {
              if (curMnode.instructions.get(i).getOpcode() == 25 && curMnode.instructions.get(i + 1).getType() == 9 && ((LdcInsnNode)curMnode.instructions.get(i + 1)).cst.equals("Illegal stance") && curMnode.instructions.get(i + 2).getType() == 5 && ((MethodInsnNode)curMnode.instructions.get(i + 2)).name.equals(SMCoreObfuscaHelper.getProperName("kickPlayerFromServer")) && ((MethodInsnNode)curMnode.instructions.get(i + 2)).desc.equals(SMCoreObfuscaHelper.getProperDesc("kickPlayerFromServer"))) {

                LabelNode labelJump = null;
                for (int k = i - 1; k >= 0; k--) {
                  if (curMnode.instructions.get(k).getType() == 7) {
                    labelJump = ((JumpInsnNode)curMnode.instructions.get(k)).label;
                  }
                } 
                
                if (labelJump != null) {
                  
                  InsnList overrideList = new InsnList();
                  overrideList.add((AbstractInsnNode)new MethodInsnNode(184, "jp/mc/ancientred/starminer/core/TransformServerHelper", "jumpOverKickIllegalStance", "()Z"));

                  overrideList.add((AbstractInsnNode)new JumpInsnNode(154, labelJump));
                  
                  curMnode.instructions.insert(curMnode.instructions.get(i - 1), overrideList);
                  LogSucceed(lablelAsTarget + "(A)");
                } 
                
                break;
              } 
            } 
            
            Double tgtCst = new Double("-0.03125");
            for (int j = 0; j < curMnode.instructions.size(); j++) {
              if (curMnode.instructions.get(j).getType() == 2 && curMnode.instructions.get(j + 1).getType() == 9 && ((LdcInsnNode)curMnode.instructions.get(j + 1)).cst.equals(tgtCst) && curMnode.instructions.get(j + 2).getOpcode() == 151 && curMnode.instructions.get(j + 3).getType() == 7) {

                LabelNode labelJump = ((JumpInsnNode)curMnode.instructions.get(j + 3)).label;

                InsnList overrideList = new InsnList();
                overrideList.add((AbstractInsnNode)new VarInsnNode(25, 0));
                overrideList.add((AbstractInsnNode)new MethodInsnNode(184, "jp/mc/ancientred/starminer/core/TransformServerHelper", "jumpOverKickFloatTooLong", "(Lnet/minecraft/network/NetHandlerPlayServer;)Z"));

                overrideList.add((AbstractInsnNode)new JumpInsnNode(154, labelJump));
                
                curMnode.instructions.insert(curMnode.instructions.get(j + 3), overrideList);
                LogSucceed(lablelAsTarget + "(B)");
                break;
              } 
            } 
          } 
        } 
      } catch (Exception e) {
        e.printStackTrace();
        LogFailed(lablelAsTarget);
        return bytes;
      } 

      ClassWriter cw = new ClassWriter(1);
      classNode.accept(cw);
      bytes = cw.toByteArray();
    } 

    if (transformedName.equals("net.minecraft.network.play.server.S0APacketUseBed")) {
      ClassReader cr = new ClassReader(bytes);
      ClassNode classNode = new ClassNode();
      cr.accept((ClassVisitor)classNode, 0);
      
      String lablelAsTarget = "S0APacketUseBed.readPacketData";
      
      try {
        String targetMethodName = SMCoreObfuscaHelper.getProperName("readPacketData");
        String targetMethoddesc = SMCoreObfuscaHelper.getProperDesc("readPacketData");

        
        for (MethodNode curMnode : classNode.methods) {
          if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {
            LogFound(lablelAsTarget);
            for (int i = curMnode.instructions.size() - 1; i >= 0; i--) {
              if (curMnode.instructions.get(i).getType() == 5 && ((MethodInsnNode)curMnode.instructions.get(i)).name.equals(SMCoreObfuscaHelper.getProperName("readByte")) && ((MethodInsnNode)curMnode.instructions.get(i)).desc.equals(SMCoreObfuscaHelper.getProperDesc("readByte"))) {


                
                ((MethodInsnNode)curMnode.instructions.get(i)).name = SMCoreObfuscaHelper.getProperName("readInt");
                ((MethodInsnNode)curMnode.instructions.get(i)).desc = SMCoreObfuscaHelper.getProperDesc("readInt");
                
                LogSucceed(lablelAsTarget);
                break;
              } 
            } 
          } 
        } 
        lablelAsTarget = "S0APacketUseBed.writePacketData";
        targetMethodName = SMCoreObfuscaHelper.getProperName("writePacketData");
        targetMethoddesc = SMCoreObfuscaHelper.getProperDesc("writePacketData");

        
        for (MethodNode curMnode : classNode.methods) {
          if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {
            LogFound(lablelAsTarget);
            for (int i = curMnode.instructions.size() - 1; i >= 0; i--) {
              if (curMnode.instructions.get(i).getType() == 5 && ((MethodInsnNode)curMnode.instructions.get(i)).name.equals(SMCoreObfuscaHelper.getProperName("writeByte")) && ((MethodInsnNode)curMnode.instructions.get(i)).desc.equals(SMCoreObfuscaHelper.getProperDesc("writeByte"))) {


                
                ((MethodInsnNode)curMnode.instructions.get(i)).name = SMCoreObfuscaHelper.getProperName("writeInt");
                ((MethodInsnNode)curMnode.instructions.get(i)).desc = SMCoreObfuscaHelper.getProperDesc("writeInt");
                
                LogSucceed(lablelAsTarget);
                break;
              } 
            } 
          } 
        } 
      } catch (Exception e) {
        e.printStackTrace();
        LogFailed(lablelAsTarget);
        return bytes;
      } 

      ClassWriter cw = new ClassWriter(1);
      classNode.accept(cw);
      bytes = cw.toByteArray();
    } 
    
    return bytes;
  }
  
  private void checkTweakerLoad() {
    try {
      Class.forName("jp.mc.ancientred.starminer.tweaker.SMAvoidOptiFineOverwriteTweaker");
      this.hasTweakerLoaded = true;
    } catch (ClassNotFoundException e) {
      this.hasTweakerLoaded = false;
    } 
  }

  public static MethodNode createRotateCorpsePublic() {
    MethodNode methodNode = new MethodNode(262144, 1, "rotateCorpsePublic", "(Lnet/minecraft/entity/EntityLivingBase;[F)V", null, null);
    
    InsnList list = methodNode.instructions;
    
    list.add((AbstractInsnNode)new VarInsnNode(25, 0));
    list.add((AbstractInsnNode)new VarInsnNode(25, 1));
    list.add((AbstractInsnNode)new VarInsnNode(25, 2));
    list.add((AbstractInsnNode)new InsnNode(3));
    list.add((AbstractInsnNode)new InsnNode(48));
    list.add((AbstractInsnNode)new VarInsnNode(25, 2));
    list.add((AbstractInsnNode)new InsnNode(4));
    list.add((AbstractInsnNode)new InsnNode(48));
    list.add((AbstractInsnNode)new VarInsnNode(25, 2));
    list.add((AbstractInsnNode)new InsnNode(5));
    list.add((AbstractInsnNode)new InsnNode(48));
    list.add((AbstractInsnNode)new MethodInsnNode(182, "net/minecraft/client/renderer/entity/RendererLivingEntity", SMCoreObfuscaHelper.getSrgName("rotateCorpse"), "(Lnet/minecraft/entity/EntityLivingBase;FFF)V"));
    list.add((AbstractInsnNode)new InsnNode(177));
    
    return methodNode;
  }
  
  public static void LogSucceed(String targetClassAndMethodName) {
    LogWrapper.log(Level.INFO, "[Starminer]Injected additional code to " + targetClassAndMethodName, new Object[0]);
  }
  public static void LogFailed(String targetClassAndMethodName) {
    LogWrapper.log(Level.INFO, "[Starminer]!!!!Logic injection failed on " + targetClassAndMethodName, new Object[0]);
  }
  public static void LogFound(String targetClassAndMethodName) {
    LogWrapper.log(Level.INFO, "[Starminer]Target method found : " + targetClassAndMethodName, new Object[0]);
  }
}
