package frc.utils;

public final class States {
    public enum ShooterState {
        ZERO(0), HALF(1), FULL(2), GOAL(3);
        public int val;
        private ShooterState(int val) {
            this.val = val;
        }
    }
}
