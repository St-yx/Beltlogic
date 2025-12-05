## Beltlogic

This Project aims to simulate a production line consisting of machines and belt segments in a self-determined order.

### Machines

To simulate processing, the machine will hold the part for a pr-determined cycle time and then release it to the following belt. Only one part can be processed in a machine at a time, following parts will wait on the belt in front of the machine.

### Conveyor Belts

Belts are used to transport parts through the production line.
Multiple parts can be on one belt at a time and will all be transported if the belt is running. A belt consists of length-units which can also be chosen freely.

Additionally a belt will have an internal logic to determine its behaviour:
    - Sensor A decides if parts are on the belt that need to be transported
    - Sensor B decides if there is one or more parts in closely in front of the belt, which would prevent it from stopping


### Visualization

After starting the simulation a terminal-based overview of the line as well as informations about part positions, belt states and machine timings is generated.
A more graphical overview and the possibility to change the line mid run is planned for the future.