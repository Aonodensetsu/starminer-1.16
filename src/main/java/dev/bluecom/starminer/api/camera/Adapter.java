package dev.bluecom.starminer.api.camera;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.vector.Vector3d;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.function.IntSupplier;

public class Adapter {
    public static class Client {
        public static final Adapter.Client INSTANCE = new Adapter.Client();

        public boolean isFirstPerson() { return (Minecraft.getInstance()).options.getCameraType().isFirstPerson(); }

        public boolean isCameraMirrored() { return (Minecraft.getInstance()).options.getCameraType().isMirrored(); }

        public PlayerEntity getPlayer() { return (Minecraft.getInstance()).player; }

        public void updateHitResult() { (Minecraft.getInstance()).gameRenderer.pick(1); }

        public boolean isMousePressed() { return (isUsePressed() || isAttackPressed()); }

        public boolean isUsePressed() { return (Minecraft.getInstance()).options.keyUse.isDown(); }

        public boolean isAttackPressed() { return (Minecraft.getInstance()).options.keyAttack.isDown(); }
    }

    public static class Player {
        public final PlayerEntity player;

        public Player(PlayerEntity player) { this.player = player; }

        public float getRotationYaw() { return this.player.yRot; }

        public float getPrevRotationYaw() { return this.player.yRotO; }

        public void setRotationYaw(float value) { this.player.yRot = value; }

        public void setPrevRotationYaw(float value) { this.player.yRotO = value; }

        public Adapter.Position getPosition() { return new Position(this.player.position()); }

        public boolean isPassenger() { return this.player.isPassenger(); }

        public boolean isUsingItem() { return this.player.isUsingItem(); }

        public void setRotationYawSafe(float value) { setRotationYaw(AngleHelper.wrapAngle(getRotationYaw(), value)); }
    }

    public static class Input {
        private final MovementInput input;

        public Input(MovementInput input) { this.input = input; }

        public boolean isLeftKeyDown() { return this.input.left; }

        public boolean isRightKeyDown() { return this.input.right; }

        public boolean isForwardKeyDown() { return this.input.up; }

        public boolean isBackKeyDown() { return this.input.down; }

        public void setLeftKeyDown(boolean value) { this.input.left = value; }

        public void setRightKeyDown(boolean value) { this.input.right = value; }

        public void setForwardKeyDown(boolean value) { this.input.up = value; }

        public void setBackKeyDown(boolean value) { this.input.down = value; }

        public float getMoveForward() { return this.input.forwardImpulse; }

        public float getMoveStrafe() { return this.input.leftImpulse; }

        public void setMoveForward(float value) { this.input.forwardImpulse = value; }

        public void setMoveStrafe(float value) { this.input.leftImpulse = value; }
    }

    public static class Position {
        private final Vector3d position;

        public Position(Vector3d position) { this.position = position; }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position that = (Position)o;
            return Objects.equals(this.position, that.position);
        }

        public int hashCode() { return Objects.hash(this.position); }
    }

    public static class AngleHelper {
        public static float smoothAngle(float partialTicks, float prevAngle, float currentAngle) {
            float anglesDiff = (currentAngle - prevAngle + 180.0F) % 360.0F - 180.0F;
            return prevAngle + partialTicks * ((anglesDiff < -180.0F) ? (anglesDiff + 360.0F) : anglesDiff);
        }

        public static float wrapAngle(float prevAngle, float currentAngle) {
            float anglesDiff = (currentAngle - prevAngle + 180.0F) % 360.0F - 180.0F;
            return prevAngle + ((anglesDiff < -180.0F) ? (anglesDiff + 360.0F) : anglesDiff);
        }
    }

    public static class MouseAction {
        private final Runnable action;

        public MouseAction(Runnable action) {
            this.action = action;
        }

        public void play() {
            this.action.run();
        }
    }

    public static class RepeatedUse extends MouseAction {
        private final Adapter.Client client;
        private final IntSupplier itemUseCooldown;

        public RepeatedUse(Adapter.Client client, IntSupplier itemUseCooldown, Runnable action) {
            super(action);
            this.client = client;
            this.itemUseCooldown = itemUseCooldown;
        }

        public void play() {
            if (this.client.isUsePressed() && this.itemUseCooldown.getAsInt() == 0 && !this.client.getPlayer().isUsingItem())
                super.play();
        }
    }

    public static class DelayedActions {
        private final Queue<MouseAction> actionQueue = new ArrayDeque<>();
        private boolean replayingActions = false;

        public void writeAction(MouseAction action) {
            this.actionQueue.add(action);
        }

        public void replayActions() {
            this.replayingActions = true;
            try {
                while (!this.actionQueue.isEmpty()) {
                    this.actionQueue.remove().play();
                }
            } finally {
                this.replayingActions = false;
            }
        }

        public boolean isReplayingActions() {
            return this.replayingActions;
        }
    }

    public enum TickPhase { START, END }
}
