package com.artemchep.keyguard.common.usecase

import com.artemchep.keyguard.common.io.IO

interface RePromptCipherById : (
    Set<String>,
    Boolean,
) -> IO<Unit>
