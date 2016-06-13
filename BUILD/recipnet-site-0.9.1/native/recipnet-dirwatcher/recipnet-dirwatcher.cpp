/*
 * Reciprocal Net project
 * @(#)recipnet-dirwatcher.cpp
 *
 * 13-Aug-2004: cwestnea wrote first draft
 *
 * Helper program which registers watches on files input through stdin and 
 * then reports notifications through stdout. Valid commands are:
 * watch <sampleId> <directoryPath>
 * unwatch <sampleId>
 * quit
 *
 * This apps reports notifications and errors in this format:
 * notify: <sampleId>
 * ack: <sampleId>
 * full: <sampleId>
 * failure: <sampleId> <message>
 * nonfatal: <message>
 * fatal: <message>
 *
 * Fatal errors are accompanied by an exit code from the program. Here are the 
 * exit codes:
 * 0 - normal
 * 1 - pipe closed
 * 2 - system file limit reached
 * 3 - unable to register signal handler
 */
#include <errno.h>
#include <limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "WatchListener.h"

// handlers defined in objects tend to be segfault prone
void notify_handler(int sig, siginfo_t *si, void *context);

/**
 * Entry point; creates the <code>WatchListener</code> and processes input.
 */
int main() {
    // Install the signal handler
    WatchListener::setHandler(notify_handler);

    // this is the format for the watch command, can't use defines in a string
    // so we print the string and use it later.
    char watchCommand[40];
    sprintf(watchCommand, "watch %%d %%%ds\n", PATH_MAX - 1);

    // loop over stdin
    char incomingCommand[PATH_MAX + 24];
    WatchListener* watchListener = WatchListener::getInstance();
    while (!feof(stdin) && !feof(stdout)) {
        // get input, fgets guarantees a null char at the end of the string
        if (fgets(incomingCommand, sizeof(incomingCommand), stdin) == NULL) {
            if (ferror(stdin)) {
                // Errors are unexpected except for interrupt notifications, 
                // which are a byproduct of directory notifications. If we get 
                // an error other than interrupts just alert the user. EOFs are
                // not considered errors.
                if (errno != EINTR) {
                    printf("nonfatal: %s\n", strerror(errno));
                }
                clearerr(stdin);
                fflush(stdout);
            } 
            // probably end of file, or interrupt, or something
            continue;
        }

        // process input, the return val of sscanf is the # of items assigned
        int sampleId;
        char dirname[PATH_MAX];
        if (sscanf(incomingCommand, watchCommand, &sampleId, dirname) == 2) {
            // "watch" command
            // make sure that dirname is null terminated
            dirname[PATH_MAX - 1] = 0;
            if (watchListener->watch(sampleId, dirname) == 0) {
                printf("ack: %d\n", sampleId);
                fflush(stdout);
            }
        } else if (sscanf(incomingCommand, "unwatch %d\n", &sampleId) == 1) {
            // "unwatch" command
            watchListener->unwatch(sampleId);
        } else if (!strcmp(incomingCommand, "quit\n")) {
            // "quit" command
            // normal exit
            return 0;
        } else {
            printf("nonfatal: illegal command %s", incomingCommand);
            fflush(stdout);
        }
    }
 
    // the pipe has abnormally closed, exit
    printf("fatal: pipe closed\n");
    fflush(stdout);
    return 1;
}

/**
 * Called by the kernel when a watched directory changes. This implementation
 * calls the notify method of the WatchListener.
 */
void notify_handler(int sig, siginfo_t *si, void *context) {
    WatchListener::getInstance()->notify(si->si_fd);
}
