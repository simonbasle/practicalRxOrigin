# Step 007 - Live & Let Die (and Retry)
Start from branch `step6`, solution is in branch `step7`.

## The Problem
`StatService.lastBlockFoundBy` has a not-so fireproof implementation (choose a random `User`).

But what if… we wanted to do it without knowing the number of users in advance?

## The Plan
Generate an index randomly (within a broadly guessed limit)

Retry up to 4 times if out of bounds

Last resort: use a default `User` (eg. “Banned User”)

## Useful Operators
* `flatMap`
* `skip`
* `retry`
* `onErrorReturn`

## Trap #1
**skip doesn’t error if overflow**
>=> combine with `first`/`last` to trigger an exception

## Trap #2
**`just` caches the value**
>=> use `create` to regenerate at each retry