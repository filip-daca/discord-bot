package xyz.komeo.flow

import reactor.core.publisher.Flux

interface ConsumesFlux {
    def consume(Flux flux)
}