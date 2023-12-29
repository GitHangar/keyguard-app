package com.artemchep.keyguard.feature.confirmation.organization

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.artemchep.keyguard.feature.navigation.DialogRouteForResult
import com.artemchep.keyguard.feature.navigation.RouteResultTransmitter
import com.artemchep.keyguard.ui.SimpleNote

class OrganizationConfirmationRoute(
    val args: Args,
) : DialogRouteForResult<OrganizationConfirmationResult> {
    data class Args(
        val decor: Decor,
        val flags: Int = 0,
        // selection
        val accountId: String? = null,
        val organizationId: String? = null,
        val folderId: FolderInfo = FolderInfo.None,
        val collectionsIds: Set<String> = emptySet(),
        // blacklist
        val blacklistedAccountIds: Set<String?> = emptySet(),
        val blacklistedOrganizationIds: Set<String?> = emptySet(),
        val blacklistedCollectionIds: Set<String?> = emptySet(),
        val blacklistedFolderIds: Set<String?> = emptySet(),
    ) {
        companion object {
            private const val RAW_RO_ACCOUNT = 1
            private const val RAW_RO_ORGANIZATION = 2
            private const val RAW_RO_COLLECTION = 4
            private const val RAW_RO_FOLDER = 8

            const val RO_ACCOUNT = RAW_RO_ACCOUNT
            const val RO_ORGANIZATION = RAW_RO_ORGANIZATION
            const val RO_COLLECTION = RAW_RO_COLLECTION
            const val RO_FOLDER = RAW_RO_FOLDER
        }

        data class Decor(
            val title: String,
            val note: SimpleNote? = null,
            val icon: ImageVector? = null,
        )
    }

    @Composable
    override fun Content(
        transmitter: RouteResultTransmitter<OrganizationConfirmationResult>,
    ) {
        OrganizationConfirmationScreen(
            args = args,
            transmitter = transmitter,
        )
    }
}
