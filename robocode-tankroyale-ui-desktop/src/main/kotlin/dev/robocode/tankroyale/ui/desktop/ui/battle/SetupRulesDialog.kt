package dev.robocode.tankroyale.ui.desktop.ui.battle

import dev.robocode.tankroyale.ui.desktop.extensions.JComponentExt.addButton
import dev.robocode.tankroyale.ui.desktop.extensions.JComponentExt.addLabel
import dev.robocode.tankroyale.ui.desktop.extensions.JComponentExt.showMessage
import dev.robocode.tankroyale.ui.desktop.extensions.JTextFieldExt.onChange
import dev.robocode.tankroyale.ui.desktop.extensions.JTextFieldExt.setInputVerifier
import dev.robocode.tankroyale.ui.desktop.model.IGameSetup
import dev.robocode.tankroyale.ui.desktop.settings.GameType
import dev.robocode.tankroyale.ui.desktop.settings.GamesSettings
import dev.robocode.tankroyale.ui.desktop.settings.MutableGameSetup
import dev.robocode.tankroyale.ui.desktop.ui.GameConstants
import dev.robocode.tankroyale.ui.desktop.ui.MainWindow
import dev.robocode.tankroyale.ui.desktop.ui.ResourceBundles
import dev.robocode.tankroyale.ui.desktop.util.Event
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import net.miginfocom.swing.MigLayout
import java.awt.Dimension
import java.awt.EventQueue
import javax.swing.*

@UnstableDefault
@ImplicitReflectionSerializer
object SetupRulesDialog : JDialog(MainWindow, ResourceBundles.UI_TITLES.get("setup_rules_dialog")) {

    private val setupRulesPanel = SetupRulesPanel()

    init {
        defaultCloseOperation = DISPOSE_ON_CLOSE

        size = Dimension(400, 350)

        setLocationRelativeTo(null) // center on screen

        contentPane.add(setupRulesPanel)
    }
}

@UnstableDefault
@ImplicitReflectionSerializer
class SetupRulesPanel : JPanel(MigLayout("fill")) {

    // Private events
    private val onOk = Event<JButton>()
    private val onCancel = Event<JButton>()
    private val onResetToDefault = Event<JButton>()
    private val onApply = Event<JButton>()

    private val gameTypeComboBox = GameTypeComboBox()
    private val widthTextField = JTextField(6)
    private val heightTextField = JTextField(6)
    private val minNumParticipantsTextField = JTextField(6)
    private val maxNumParticipantsTextField = JTextField(6)
    private val numberOfRoundsTextField = JTextField(6)
    private val gunCoolingRateTextField = JTextField(6)
    private val inactivityTurnsTextField = JTextField(6)
    private val readyTimeoutTextField = JTextField(6)
    private val turnTimeoutTextField = JTextField(6)

    private var changed = false

    private val gameSetup: MutableGameSetup
        get() = GamesSettings.games[gameTypeComboBox.selectedGameType]!!

    private var lastGameSetup: IGameSetup = gameSetup.copy()

    private val okButton: JButton
    private val applyButton: JButton

    init {
        val commonPanel = JPanel(MigLayout()).apply {
            addLabel("game_type")
            add(gameTypeComboBox, "wrap")

            addLabel("min_num_of_participants")
            add(minNumParticipantsTextField, "wrap")

            addLabel("max_num_of_participants")
            add(maxNumParticipantsTextField, "wrap")

            addLabel("number_of_rounds")
            add(numberOfRoundsTextField, "wrap")

            addLabel("gun_cooling_rate")
            add(gunCoolingRateTextField, "wrap")

            addLabel("max_inactivity_turns")
            add(inactivityTurnsTextField, "wrap")

            addLabel("ready_timeout")
            add(readyTimeoutTextField, "wrap")

            addLabel("turn_timeout")
            add(turnTimeoutTextField, "wrap")
        }
        val arenaPanel = JPanel(MigLayout()).apply {
            border = BorderFactory.createTitledBorder(ResourceBundles.STRINGS.get("arena_size"))

            addLabel("width")
            add(widthTextField, "wrap")
            addLabel("height")
            add(heightTextField)
        }
        val upperPanel = JPanel(MigLayout()).apply {
            add(commonPanel, "west")
            add(arenaPanel, "east")
        }
        val lowerPanel = JPanel(MigLayout()).apply {
            okButton = addButton("ok", onOk, "tag ok")
            addButton("cancel", onCancel, "tag cancel")
            addButton("reset_to_default", onResetToDefault, "")
            applyButton = addButton("apply", onApply, "tag apply")
        }
        SetupRulesDialog.rootPane.defaultButton = okButton

        gameTypeComboBox.addItemListener {
            updateFieldsForGameType()
        }
        gameTypeComboBox.setSelectedGameType(GameType.CLASSIC)
        updateFieldsForGameType()

        applyButton.isVisible = false

        add(upperPanel, "center, wrap")
        add(lowerPanel, "center")

        widthTextField.setInputVerifier { widthVerifier() }
        heightTextField.setInputVerifier { heightVerifier() }
        minNumParticipantsTextField.setInputVerifier { minNumParticipantsVerifier() }
        maxNumParticipantsTextField.setInputVerifier { maxNumParticipantsVerifier() }
        numberOfRoundsTextField.setInputVerifier { numberOfRoundsVerifier() }
        gunCoolingRateTextField.setInputVerifier { gunCoolingRateVerifier() }
        inactivityTurnsTextField.setInputVerifier { inactivityTurnsVerifier() }
        readyTimeoutTextField.setInputVerifier { readyTimeoutVerifier() }
        turnTimeoutTextField.setInputVerifier { turnTimeoutVerifier() }

        onOk.subscribe {
            apply()
            SetupRulesDialog.dispose()
        }
        onApply.subscribe {
            apply()
        }
        onCancel.subscribe {
            lastGameSetup = gameSetup
            SetupRulesDialog.dispose()
        }
        onResetToDefault.subscribe {
            val selectedGameType = gameTypeComboBox.selectedGameType
            val default: MutableGameSetup? = GamesSettings.defaultGameSetup[selectedGameType]?.toMutableGameSetup()
            if (default != null) {
                GamesSettings.games[selectedGameType]?.copy(default)
                updateFieldsForGameType()
            }
        }

        listOf(
            widthTextField,
            widthTextField,
            heightTextField,
            minNumParticipantsTextField,
            maxNumParticipantsTextField,
            numberOfRoundsTextField,
            gunCoolingRateTextField,
            inactivityTurnsTextField,
            readyTimeoutTextField,
            turnTimeoutTextField
        ).forEach { it.onChange { handleChange() } }
    }

    private fun apply() {
        GamesSettings.save()
        changed = false
        applyButton.isEnabled = false
        lastGameSetup = gameSetup
    }

    private fun handleChange() {
        changed = isChanged()
        if (changed) applyButton.isVisible = true
        applyButton.isEnabled = changed
    }

    private fun isChanged(): Boolean =
        widthTextField.text != gameSetup.arenaWidth.toString() ||
                heightTextField.text != gameSetup.arenaHeight.toString() ||
                minNumParticipantsTextField.text != gameSetup.minNumberOfParticipants.toString() ||
                maxNumParticipantsTextField.text != gameSetup.maxNumberOfParticipants?.toString() ?: "" ||
                numberOfRoundsTextField.text != gameSetup.numberOfRounds.toString() ||
                inactivityTurnsTextField.text != gameSetup.maxInactivityTurns.toString() ||
                gunCoolingRateTextField.text != gameSetup.gunCoolingRate.toString() ||
                readyTimeoutTextField.text != gameSetup.readyTimeout.toString() ||
                turnTimeoutTextField.text != gameSetup.turnTimeout.toString()

    private fun updateFieldsForGameType() {
        widthTextField.text = gameSetup.arenaWidth.toString()
        heightTextField.text = gameSetup.arenaHeight.toString()
        minNumParticipantsTextField.text = gameSetup.minNumberOfParticipants.toString()
        maxNumParticipantsTextField.text = gameSetup.maxNumberOfParticipants?.toString() ?: ""
        numberOfRoundsTextField.text = gameSetup.numberOfRounds.toString()
        inactivityTurnsTextField.text = gameSetup.maxInactivityTurns.toString()
        gunCoolingRateTextField.text = gameSetup.gunCoolingRate.toString()
        readyTimeoutTextField.text = gameSetup.readyTimeout.toString()
        turnTimeoutTextField.text = gameSetup.turnTimeout.toString()
    }

    private fun widthVerifier(): Boolean {
        val width: Int? = try {
            widthTextField.text.trim().toInt()
        } catch (e: NumberFormatException) {
            null
        }
        val valid = width != null && width in GameConstants.MIN_ARENA_SIZE..GameConstants.MAX_ARENA_SIZE
        if (valid && width != null) {
            gameSetup.arenaWidth = width
        } else {
            showMessage(
                String.format(
                    ResourceBundles.MESSAGES.get("arena_size_range"),
                    GameConstants.MIN_ARENA_SIZE,
                    GameConstants.MAX_ARENA_SIZE
                )
            )

            widthTextField.text = "" + gameSetup.arenaWidth
        }
        return valid
    }

    private fun heightVerifier(): Boolean {
        val height: Int? = try {
            heightTextField.text.trim().toInt()
        } catch (e: NumberFormatException) {
            null
        }
        val valid = height != null && height in GameConstants.MIN_ARENA_SIZE..GameConstants.MAX_ARENA_SIZE
        if (valid && height != null) {
            gameSetup.arenaHeight = height
        } else {
            showMessage(
                String.format(
                    ResourceBundles.MESSAGES.get("arena_size_range"),
                    GameConstants.MIN_ARENA_SIZE,
                    GameConstants.MAX_ARENA_SIZE
                )
            )

            heightTextField.text = "" + gameSetup.arenaHeight
        }
        return valid
    }

    private fun minNumParticipantsVerifier(): Boolean {
        val minNum: Int? = try {
            minNumParticipantsTextField.text.trim().toInt()
        } catch (e: NumberFormatException) {
            null
        }
        val valid = minNum != null && minNum in GameConstants.MIN_NUM_PARTICIPANTS..GameConstants.MAX_NUM_PARTICIPANTS
        if (valid && minNum != null) {
            gameSetup.minNumberOfParticipants = minNum
        } else {
            showMessage(
                String.format(
                    ResourceBundles.MESSAGES.get("min_num_participants"),
                    GameConstants.MIN_NUM_PARTICIPANTS
                )
            )

            minNumParticipantsTextField.text = "" + gameSetup.minNumberOfParticipants
        }
        return valid
    }

    private fun maxNumParticipantsVerifier(): Boolean {
        if (maxNumParticipantsTextField.text.isBlank()) {
            gameSetup.maxNumberOfParticipants = null
            return true
        }
        val minNum: Int? = try {
            minNumParticipantsTextField.text.trim().toInt()
        } catch (e: NumberFormatException) {
            null
        }
        val maxNum: Int? = try {
            maxNumParticipantsTextField.text.trim().toInt()
        } catch (e: NumberFormatException) {
            null
        }
        val valid = minNum != null && maxNum != null &&
                (minNum in GameConstants.MIN_NUM_PARTICIPANTS..GameConstants.MAX_NUM_PARTICIPANTS) && (maxNum in minNum..GameConstants.MAX_NUM_PARTICIPANTS)
        if (valid && maxNum != null) {
            gameSetup.maxNumberOfParticipants = maxNum
        } else {
            if (maxNum == null || maxNum > GameConstants.MAX_NUM_PARTICIPANTS) {
                showMessage(
                    String.format(
                        ResourceBundles.MESSAGES.get("max_num_participants"),
                        GameConstants.MAX_NUM_PARTICIPANTS
                    )
                )
            } else {
                showMessage(ResourceBundles.MESSAGES.get("max_num_participants_too_small"))
            }
            maxNumParticipantsTextField.text =
                if (gameSetup.maxNumberOfParticipants == null) "" else "${gameSetup.maxNumberOfParticipants}"
        }
        return valid
    }

    private fun numberOfRoundsVerifier(): Boolean {
        val numRounds: Int? = try {
            numberOfRoundsTextField.text.trim().toInt()
        } catch (e: NumberFormatException) {
            null
        }
        val valid = numRounds != null && numRounds in 1..GameConstants.MAX_NUM_ROUNDS
        if (valid && numRounds != null) {
            gameSetup.numberOfRounds = numRounds
        } else {
            showMessage(String.format(ResourceBundles.MESSAGES.get("num_rounds_range"), GameConstants.MAX_NUM_ROUNDS))

            maxNumParticipantsTextField.text = "" + gameSetup.numberOfRounds
        }
        return valid
    }

    private fun gunCoolingRateVerifier(): Boolean {
        val rate: Double? = try {
            gunCoolingRateTextField.text.trim().toDouble()
        } catch (e: NumberFormatException) {
            null
        }
        val valid = rate != null && rate > 0 && rate <= GameConstants.MAX_GUN_COOLING
        if (valid && rate != null) {
            gameSetup.gunCoolingRate = rate
        } else {
            showMessage(
                String.format(
                    ResourceBundles.MESSAGES.get("gun_cooling_range"),
                    "" + GameConstants.MAX_GUN_COOLING
                )
            )

            gunCoolingRateTextField.text = "" + gameSetup.gunCoolingRate
        }
        return valid
    }

    private fun inactivityTurnsVerifier(): Boolean {
        val turns: Int? = try {
            inactivityTurnsTextField.text.trim().toInt()
        } catch (e: NumberFormatException) {
            null
        }
        val valid = turns != null && turns in 0..GameConstants.MAX_INACTIVITY_TURNS
        if (valid && turns != null) {
            gameSetup.maxInactivityTurns = turns
        } else {
            showMessage(
                String.format(
                    ResourceBundles.MESSAGES.get("num_inactivity_turns_range"),
                    GameConstants.MAX_INACTIVITY_TURNS
                )
            )

            inactivityTurnsTextField.text = "" + gameSetup.maxInactivityTurns
        }
        return valid
    }

    private fun readyTimeoutVerifier(): Boolean {
        val timeout: Int? = try {
            readyTimeoutTextField.text.trim().toInt()
        } catch (e: NumberFormatException) {
            null
        }
        val valid = timeout != null && timeout >= 0
        if (valid && timeout != null) {
            gameSetup.readyTimeout = timeout
        } else {
            showMessage(
                String.format(
                    ResourceBundles.MESSAGES.get("ready_timeout_range"),
                    GameConstants.MAX_READY_TIMEOUT
                )
            )

            readyTimeoutTextField.text = "" + gameSetup.readyTimeout
        }
        return valid
    }

    private fun turnTimeoutVerifier(): Boolean {
        val timeout: Int? = try {
            turnTimeoutTextField.text.trim().toInt()
        } catch (e: NumberFormatException) {
            null
        }
        val valid = timeout != null && timeout >= 0
        if (valid && timeout != null) {
            gameSetup.turnTimeout = timeout
        } else {
            showMessage(
                String.format(
                    ResourceBundles.MESSAGES.get("turn_timeout_range"),
                    GameConstants.MAX_TURN_TIMEOUT
                )
            )

            turnTimeoutTextField.text = "" + gameSetup.turnTimeout
        }
        return valid
    }
}

@UnstableDefault
@ImplicitReflectionSerializer
private fun main() {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    EventQueue.invokeLater {
        SetupRulesDialog.isVisible = true
    }
}