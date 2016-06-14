/*
 * Reciprocal Net project
 * @(#)WatchListener.cpp
 *
 * 13-Aug-2004: cwestnea wrote first draft
 *
 * The WatchListener class contains all the methods necessary to act as a 
 * conduit between the user and the kernel.
 */
#include <errno.h>
#include <fcntl.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "WatchListener.h"

/* 16-Oct-2015: yuma added include <string.h> */
#include <string.h>

/**
 * Installs the signal handler that will be called by the kernel when a 
 * directory notification arrives.
 * @param notify_handler function pointer to the kernel callback as defined by
 *     sigaction()
 */
void WatchListener::setHandler(
        void (*notify_handler)(int, siginfo_t *, void *)){
    // set up the signal handler
    struct sigaction act;
    sigemptyset(&act.sa_mask);
    act.sa_flags = SA_SIGINFO;
    act.sa_sigaction = notify_handler;
    if (sigaction(SIGRTMIN, &act, NULL) < 0) {
        printf("fatal: unable to register signal handler\n");
        fflush(stdout);
        exit(3);
    }
}

/**
 * Gets the one and only instance of WatchListener, creating it if it doesn't
 * already exist.
 */
WatchListener* WatchListener::getInstance() {
    static WatchListener watcher;
    return &watcher;
}

/**
 * Places a watch on a directory and associates the file descriptor with 
 * the sample id.
 * @return 0 if sucessful, -1 otherwise
 */
int WatchListener::watch(int sampleId, const char* directoryToWatch) {
    int fileDescriptor = open(directoryToWatch, O_RDONLY);
    if (fileDescriptor < 0) {
        if (errno == EMFILE) {
            // process file limit reached
            printf("full: %d\n", sampleId);
            fflush(stdout);
            return -1;
        } else if (errno == ENFILE) {
            // system file limit reached
            printf("fatal: %d %s\n", sampleId, strerror(errno));
            fflush(stdout);
            exit(2);
        } else {
            // other failure
            printf("failure: %d %s\n", sampleId, strerror(errno));
            fflush(stdout);
            return -1;
        }
    }
    
    // turn on notification, don't want it multishot so that multiple 
    // notifications won't take up queue slots if they come before we have sent
    // the notification on stdin
    fcntl(fileDescriptor, F_SETSIG, SIGRTMIN);
    fcntl(fileDescriptor, F_NOTIFY, DN_MODIFY|DN_CREATE|DN_DELETE|DN_RENAME);

    // add sample id to hash table for quick recall
    this->sampleIdToFileDescriptor[sampleId] = fileDescriptor;
    this->fileDescriptorToSampleId[fileDescriptor] = sampleId;
    return 0;
}

/**
 * Turns off a watch for a given sample id.
 */
void WatchListener::unwatch(int sampleId) {
    int fileDescriptor = this->sampleIdToFileDescriptor[sampleId];
    if (fileDescriptor == 0) {
        return;
    }
    this->sampleIdToFileDescriptor[sampleId] = 0;
    fcntl(fileDescriptor, F_NOTIFY, 0);
    close(fileDescriptor);
}

/**
 * Called when a watched directory changes; sends a message saying what sample
 * id has changed.
 */
void WatchListener::notify(int fileDescriptor) {
    int sampleId = this->fileDescriptorToSampleId[fileDescriptor];

    // turn notification handling back on before we send the notification 
    // message to the user so we can receive new notifications from the kernel 
    // about this directory 
    fcntl(fileDescriptor, F_NOTIFY, DN_MODIFY|DN_CREATE|DN_DELETE|DN_RENAME);
    printf("notify: %d\n", sampleId);
    fflush(stdout);
}
