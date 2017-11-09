package io.alephtwo

import twitter4j.TwitterFactory

fun main(args: Array<String>) {
    val twitter = TwitterFactory.getSingleton()
    twitter.onRateLimitReached { e ->
        Thread.sleep((e.rateLimitStatus.resetTimeInSeconds * 1000).toLong())
    }

    val kevin = twitter.showUser("kevinbacon")

    blockTree(userId = kevin.id).forEach { twitter.createBlock(it) }
}

fun blockTree (userId: Long, depth: Int = 0, foundSoFar: Set<Long> = emptySet()): Set<Long> {
    // Only six degrees now
    if (depth > 5) {
        return emptySet()
    }

    // Get the followers of this user
    val twitter = TwitterFactory.getSingleton()
    val followerIds = twitter.getFollowersIDs(userId)

    // Aggregate the IDs that we have just found
    var foundIncludingNow: Set<Long> = HashSet(foundSoFar)
    while (followerIds.hasNext()) {
        foundIncludingNow = foundIncludingNow.plus(followerIds.iDs.asIterable())
    }

    // Check if we found anything new and run the block tree over it
    val subtree = foundIncludingNow
        .minus(foundSoFar)
        .map { blockTree(userId = it, depth = depth + 1, foundSoFar = foundIncludingNow) }
        .flatMapTo(HashSet(), { it })

    return foundSoFar.plus(subtree)
}

