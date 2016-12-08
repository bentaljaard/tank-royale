package net.robocode2.model;

public final class Score {

	private final double survival;
	private final double lastSurvivorBonus;
	private final double bulletDamage;
	private final double bulletKillBonus;
	private final double ramDamage;
	private final double ramKillBonus;

	public Score(double survival, double lastSurvivorBonus, double bulletDamage, double bulletKillBonus,
			double ramDamage, double ramKillBonus) {

		this.survival = survival;
		this.lastSurvivorBonus = lastSurvivorBonus;
		this.bulletDamage = bulletDamage;
		this.bulletKillBonus = bulletKillBonus;
		this.ramDamage = ramDamage;
		this.ramKillBonus = ramKillBonus;
	}

	public double getTotalScore() {
		return survival + lastSurvivorBonus + bulletDamage + bulletKillBonus + ramDamage + ramKillBonus;
	}

	public double getSurvival() {
		return survival;
	}

	public double getLastSurvivorBonus() {
		return lastSurvivorBonus;
	}

	public double getBulletDamage() {
		return bulletDamage;
	}

	public double getBulletKillBonus() {
		return bulletKillBonus;
	}

	public double getRamDamage() {
		return ramDamage;
	}

	public double getRamKillBonus() {
		return ramKillBonus;
	}

	public static final class Builder {
		private double survival;
		private double lastSurvivorBonus;
		private double bulletDamage;
		private double bulletKillBonus;
		private double ramDamage;
		private double ramKillBonus;

		public Score build() {
			return new Score(survival, lastSurvivorBonus, bulletDamage, bulletKillBonus, ramDamage, ramKillBonus);
		}

		public Builder setSurvival(double survival) {
			this.survival = survival;
			return this;
		}

		public Builder setLastSurvivorBonus(double lastSurvivorBonus) {
			this.lastSurvivorBonus = lastSurvivorBonus;
			return this;
		}

		public Builder setBulletDamage(double bulletDamage) {
			this.bulletDamage = bulletDamage;
			return this;
		}

		public Builder setBulletKillBonus(double bulletKillBonus) {
			this.bulletKillBonus = bulletKillBonus;
			return this;
		}

		public Builder setRamDamage(double ramDamage) {
			this.ramDamage = ramDamage;
			return this;
		}

		public Builder setRamKillBonus(double ramKillBonus) {
			this.ramKillBonus = ramKillBonus;
			return this;
		}
	}
}
