package xyz.komeo.command

import spock.lang.Specification

class CountModsCommandSpec extends Specification {

    def "downloading mod count"() {
        given:
        def modCount = CountModsCommand.loadTotalModCount()

        expect:
        modCount > 0
    }

}
