/*
 * Reciprocal Net project
 * @(#)WatchListener.h
 *
 * 13-Aug-2004: cwestnea wrote first draft
 *
 * Header file for defining the WatchListener class. The WatchListener class
 * contains all the methods necessary to act as a conduit between the user and
 * the kernel.
 */
#include <signal.h>
#include <map>
using namespace std;

/**
 * This class handles all the watches and kernel interaction. We use a 
 * singleton class because the notification handler installed with the kernel
 * must have an easy way to gain access to an instance of WatchListener. We
 * only want to have one listener anyway, so this method works.
 */
class WatchListener {
    public:
        static void setHandler(
                void (*notify_handler)(int, siginfo_t *, void *));
        static WatchListener* getInstance();
        int watch(int sampleId, const char* directoryToWatch);
        void unwatch(int sampleId);
        void notify(int fileDescriptor);
    private:
        /** 
         * Allows for easy retrieval of an fd by unwatch(); populated in 
         * watch(). 
         */
        map<int, int> sampleIdToFileDescriptor;

        /** 
         * Allows for easy retrieval of an id by notify(); populated in 
         * watch(). 
         */
        map<int, int> fileDescriptorToSampleId;
};
