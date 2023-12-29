package com.artemchep.keyguard.provider.bitwarden.usecase

import com.artemchep.keyguard.common.model.DSecret
import com.artemchep.keyguard.common.usecase.CipherExpiringCheck
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import org.kodein.di.DirectDI

/**
 * @author Artem Chepurnyi
 */
class CipherExpiringCheckImpl() : CipherExpiringCheck {
    constructor(directDI: DirectDI) : this()

    private val timeZone get() = TimeZone.currentSystemDefault()

    override fun invoke(secret: DSecret, now: Instant): Instant? {
        val soon = now
            .plus(3, DateTimeUnit.MONTH, timeZone)
        return when (secret.type) {
            DSecret.Type.Login -> null
            DSecret.Type.Card -> expiringCard(secret, soon)
            DSecret.Type.Identity -> null
            DSecret.Type.SecureNote -> null
            else -> null
        }
    }

    private fun expiringCard(secret: DSecret, now: Instant): Instant? {
        val cardDate = kotlin.run {
            val card = secret.card ?: return null
            val month = card.expMonth
                ?.trim()
                ?.toIntOrNull()
            val year = card.expYear
                ?.trim()
                ?.toIntOrNull()
            // If the year is unknown then there's no way
            // we know when the card is expiring.
                ?: return null
            val finalYear = if (year <= 99) 2000 + year else year
            LocalDate(finalYear, month ?: 1, 1)
        }
        val cardInstant = cardDate
            // The card expiry date is inclusive, so the actual time
            // is the first day of the next month.
            .plus(1, DateTimeUnit.MONTH)
            .atStartOfDayIn(timeZone)
        return cardInstant.takeIf { it <= now }
    }
}
