PeerSim-Push-Sum
================================================================================

A comparison of the Push-Sum and Push-Pull algorithms using the PeerSim network
simulator.

Push-Pull [1] and Push-Sum [2] are gossip-based protocols to for calculating 
aggregates, such as the mean, among all nodes in the network. Such protocols 
are fully decentralized, based on exchanging messages with randomly selected 
neighbors, and require no knowledge about the network topology.

PeerSim [3] is an open-source, peer-to-peer network simulator, written in Java.

We study the behavior of the protocols in simplified cycle-driven and more 
realistic event-driven simulations.



Usage
--------------------------------------------------------------------------------

You can obtain the source of this project from the [Github repository]
(https://github.com/darioseidl/peersim-push-sum).

The Javadocs can be accessed at the [Github project pages]
(http://darioseidl.github.io/peersim-push-sum/doc/).

The PeerSim library must be downloaded separately from the [SourceForge 
project page](http://sourceforge.net/projects/peersim/). Extract the Zip 
archive and place the peersim-1.0.5 folder in the lib folder of this project. 

You can import the project in Eclipse, use the ant buildfile to compile and 
run the examples, or run the simulator directly.

The PeerSim simulator (peersim.Simulator) always expects a configutation 
file as parameter. The configuration files for our simulations can be found 
in the config folder.

To run the simulator use one of the following commands, for for Push-Pull 
(pp) and Push-Sum (ps), cycle-driven (cd) and event-driven (ed) respectively

	java -classpath ./bin:./lib/commons-io-2.4/commons-io-2.4.jar:./lib/commons-math3-3.2/commons-math3-3.2.jar:./lib/peersim-1.0.5/peersim-1.0.5.jar:./lib/peersim-1.0.5/djep-1.0.0.jar:./lib/peersim-1.0.5/jep-2.3.0.jar peersim.Simulator config/pp-cd.config
	java -classpath ./bin:./lib/commons-io-2.4/commons-io-2.4.jar:./lib/commons-math3-3.2/commons-math3-3.2.jar:./lib/peersim-1.0.5/peersim-1.0.5.jar:./lib/peersim-1.0.5/djep-1.0.0.jar:./lib/peersim-1.0.5/jep-2.3.0.jar peersim.Simulator config/ps-cd.config
	java -classpath ./bin:./lib/commons-io-2.4/commons-io-2.4.jar:./lib/commons-math3-3.2/commons-math3-3.2.jar:./lib/peersim-1.0.5/peersim-1.0.5.jar:./lib/peersim-1.0.5/djep-1.0.0.jar:./lib/peersim-1.0.5/jep-2.3.0.jar peersim.Simulator config/pp-ed.config
	java -classpath ./bin:./lib/commons-io-2.4/commons-io-2.4.jar:./lib/commons-math3-3.2/commons-math3-3.2.jar:./lib/peersim-1.0.5/peersim-1.0.5.jar:./lib/peersim-1.0.5/djep-1.0.0.jar:./lib/peersim-1.0.5/jep-2.3.0.jar peersim.Simulator config/ps-ed.config


The project also provides an ant buildfile to build and run the applications.
Note that for ant to work, the JAVA_HOME environment variable needs to be 
set to a JDK 6 or higher.

To see a list of all available targets, use

	ant -projecthelp

To build the project and to generate the javadocs, use

	ant build, docs

To run the simulations, use the following commands

	ant run-pp-cd
	ant run-ps-cd
	ant run-pp-ed
	ant run-ps-ed

With Gnuplot installed and the binary in your path, the following command 
will run several simulations and plot the results:

	ant all-plots

With Graphviz installed, you can plot the different network topologies using

	ant all-graphs



References
--------------------------------------------------------------------------------

[1]

[2] D. Kempe, A. Dobra and J. Gehrke, "Gossip-based Computation of Aggregate 
Information" In Proc. of the 44th Annual IEEE Symposium on Foundations of 
Computer Science (FOCS '03), pages 482–491, October, 2003.

[3] A. Montresor and M. Jelasity, "PeerSim: A Scalable P2P Simulator" 
In Proc. of the 9th Int. Conference on Peer-to-Peer (P2P’09), pages 99–100,
September 2009.

