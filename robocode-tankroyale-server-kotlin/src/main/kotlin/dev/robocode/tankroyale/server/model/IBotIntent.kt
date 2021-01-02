package dev.robocode.tankroyale.server.model

/**
 * Interface for a bot intent. Bot intents are sent from bots between turns.
 * A bot intent reflects the bot's orders for setting a new target speed, turn rates, bullet power etc.
 */
interface IBotIntent {
    /** New target speed. */
    val targetSpeed: Double?

    /** New driving turn rate. */
    val turnRate: Double?

    /** New gun turn rate. */
    val gunTurnRate: Double?

    /** New radar turn rate. */
    val radarTurnRate: Double?

    /** New bullet power. */
    val bulletPower: Double?

    /** Flag set to adjusting gun for body turn. */
    val adjustGunForBodyTurn: Boolean?

    /** Flag set to adjusting radar for gun turn. */
    val adjustRadarForGunTurn: Boolean?

    /** Flag set to perform rescan (reusing last scan direction and scan spread angle) */
    val scan: Boolean?

    /** New body color. */
    val bodyColor: String?

    /** New gun turret color. */
    val turretColor: String?

    /** New radar color. */
    val radarColor: String?

    /** New bullet color. */
    val bulletColor: String?

    /** New scan color. */
    val scanColor: String?

    /** New tracks color. */
    val tracksColor: String?

    /** New gun color. */
    val gunColor: String?
}
