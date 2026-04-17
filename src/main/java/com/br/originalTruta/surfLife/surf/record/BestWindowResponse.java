package com.br.originalTruta.surfLife.surf.record;

import java.time.OffsetDateTime;
import java.util.List;

public record BestWindowResponse(
        Long spotId,
        String spotName,
        OffsetDateTime from,
        OffsetDateTime to,
        BestWindowSlotResponse bestSlot,
        List<BestWindowSlotResponse> rankedSlots
) {
}