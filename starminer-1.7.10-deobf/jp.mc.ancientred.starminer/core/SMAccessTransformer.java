package jp.mc.ancientred.starminer.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class SMAccessTransformer
  implements IClassTransformer
{
  public byte[] transform(String name, String transformedName, byte[] bytes) {
    if (!transformedName.startsWith("net.minecraft")) return bytes;

    if (transformedName.equals("net.minecraft.entity.EntityLiving")) {
      ClassReader cr = new ClassReader(bytes);
      ClassNode classNode = new ClassNode();
      cr.accept((ClassVisitor)classNode, 0);

      classNode.superName = "jp/mc/ancientred/starminer/core/entity/EntityLivingGravitized";
      for (MethodNode curMnode : classNode.methods) {
        
        for (int i = 0; i < curMnode.instructions.size(); i++) {
          if (curMnode.instructions.get(i).getType() == 3 && ((TypeInsnNode)curMnode.instructions.get(i)).desc.equals("net/minecraft/entity/EntityLivingBase"))
          {
            ((TypeInsnNode)curMnode.instructions.get(i)).desc = "jp/mc/ancientred/starminer/core/entity/EntityLivingGravitized";
          }
          if (curMnode.instructions.get(i).getType() == 5 && ((MethodInsnNode)curMnode.instructions.get(i)).owner.equals("net/minecraft/entity/EntityLivingBase"))
          {
            ((MethodInsnNode)curMnode.instructions.get(i)).owner = "jp/mc/ancientred/starminer/core/entity/EntityLivingGravitized";
          }
          if (curMnode.instructions.get(i).getType() == 4 && ((FieldInsnNode)curMnode.instructions.get(i)).owner.equals("net/minecraft/entity/EntityLivingBase"))
          {
            ((FieldInsnNode)curMnode.instructions.get(i)).owner = "jp/mc/ancientred/starminer/core/entity/EntityLivingGravitized";
          }
        } 
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
        classNode.superName = "jp/mc/ancientred/starminer/core/entity/EntityLivingGravitized";
        for (MethodNode curMnode : classNode.methods) {
          
          for (int i = 0; i < curMnode.instructions.size(); i++) {
            if (curMnode.instructions.get(i).getType() == 3 && ((TypeInsnNode)curMnode.instructions.get(i)).desc.equals("net/minecraft/entity/EntityLivingBase"))
            {
              ((TypeInsnNode)curMnode.instructions.get(i)).desc = "jp/mc/ancientred/starminer/core/entity/EntityLivingGravitized";
            }
            if (curMnode.instructions.get(i).getType() == 5 && ((MethodInsnNode)curMnode.instructions.get(i)).owner.equals("net/minecraft/entity/EntityLivingBase"))
            {
              ((MethodInsnNode)curMnode.instructions.get(i)).owner = "jp/mc/ancientred/starminer/core/entity/EntityLivingGravitized";
            }
            if (curMnode.instructions.get(i).getType() == 4 && ((FieldInsnNode)curMnode.instructions.get(i)).owner.equals("net/minecraft/entity/EntityLivingBase"))
            {
              ((FieldInsnNode)curMnode.instructions.get(i)).owner = "jp/mc/ancientred/starminer/core/entity/EntityLivingGravitized";
            }
          } 
        } 
      } catch (Exception e) {
        e.printStackTrace();
        SMTransformer.LogFailed(lablelAsTarget);
        return bytes;
      } 
      
      ClassWriter cw = new ClassWriter(1);
      classNode.accept(cw);
      bytes = cw.toByteArray();
    } 
    
    return bytes;
  }
