package io.alephtwo

import twitter4j.TwitterFactory
import java.time.Instant

fun main(args: Array<String>) {
    val twitter = TwitterFactory.getSingleton()
    twitter.onRateLimitReached { e ->
        val resetsAt = e.rateLimitStatus.resetTimeInSeconds.toLong()
        val now = Instant.now().epochSecond
        Thread.sleep((resetsAt - now) * 1000)
    }

    val kevin = twitter.showUser("kevinbacon")
    blockTree(userId = kevin.id).forEach { twitter.createBlock(it) }
}

fun blockTree (userId: Long, depth: Int = 0, foundSoFar: Set<Long> = setOf(userId)): Set<Long> {
    // Only six degrees now
    if (depth > 5) {
        return emptySet()
    }

    // Get the followers of this user
    val twitter = TwitterFactory.getSingleton()

    // Aggregate the IDs that we have just found
    var foundIncludingNow: Set<Long> = HashSet(foundSoFar)

    var followerIds = twitter.getFollowersIDs(userId, -1)
    do {
        foundIncludingNow = foundIncludingNow.plus(followerIds.iDs.asIterable())
        followerIds = twitter.getFollowersIDs(userId, followerIds.nextCursor)
    } while (followerIds.hasNext())

    // Check if we found anything new and run the block tree over it
    val subtree = foundIncludingNow
        .minus(foundSoFar)
        .map { blockTree(userId = it, depth = depth + 1, foundSoFar = foundIncludingNow) }
        .flatMapTo(HashSet(), { it })

    return foundSoFar.plus(subtree)
}

