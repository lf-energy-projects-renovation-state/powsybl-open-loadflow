# Configuration

To use PowSyBl Open Load Flow for all security analyses, you have to configure the `security-analysis` module in your configuration file:
```yaml
security-analysis:
  default-impl-name: OpenLoadFlow
```

## Generic parameters

Generic parameters are documented in [PowSyBl Core](inv:powsyblcore:*:*#simulation/security/configuration)

The next section details the parameters that are specific to PowSyBl Open Load Flow.

## Specific parameters

**contingencyPropagation**  
The `contingencyPropagation` property is applicable only to the portions of the network modeled with a Node/Breaker representation.

Node: In iIDM, the topology modeling style Node/Breaker or Bus/Breaker is defined on a voltage level basis.
For most users, all a network is of same topology style, but hybrid representation is also supported.

For Bus/Breaker portions of the network, Security Analysis simulates the outage of only the equipment(s) defined in the contingency.

For Node/Breaker portions of the network:
- when `contingencyPropagation` is set to `false`, Security Analysis will simulate the outage of only the equipment(s) defined in the contingency.
- when `contingencyPropagation` is set to `true` (default) Security Analysis will determine by topological search the switches with type circuit breakers
(i.e. capable of opening fault currents) that must be opened to isolate the fault. Depending on the network structure,
this could lead to more equipments to be simulated as tripped, because disconnectors and load break switches
(i.e., not capable of opening fault currents) are not considered.

The default value is `true`.

**createResultExtension**  
The `createResultExtension` property defines whether Open Load Flow specific results extensions should be created
in the security analysis results. Today the available extensions provide information about branches and three-windings
transformers voltages (magnitude and angle):
- `OlfBranchResult` [![Javadoc](https://img.shields.io/badge/-javadoc-blue.svg)](https://javadoc.io/doc/com.powsybl/powsybl-open-loadflow/latest/com/powsybl/openloadflow/network/impl/OlfBranchResult.html)
- `OlfThreeWindingsTransformerResult` [![Javadoc](https://img.shields.io/badge/-javadoc-blue.svg)](https://javadoc.io/doc/com.powsybl/powsybl-open-loadflow/latest/com/powsybl/openloadflow/network/impl/OlfThreeWindingsTransformerResult.html)

The default value is `false`.

**threadCount**  
The `threadCount` property defines the number of threads used to run the security analysis (for both AC and DC). 
The parallelization is implemented at the contingency level, so the contingency list is split into `threadCount` chunks
and each chunk is ran by a different thread. 

The thread pool used for getting threads is the one provided by the `ComputationManager` [![Javadoc](https://img.shields.io/badge/-javadoc-blue.svg)](https://javadoc.io/doc/com.powsybl/powsybl-core/latest/com/powsybl/computation/ComputationManager.html) 
(see `ComputationManager.getExecutor` method). By default, when using the local computation manager, this is the `ForkJoinPool` common pool which is used.

The default value is 1.

**dcFastMode**  
The `dcFastMode` property allows to use fast DC security analysis, based on Woodbury's formula for calculating post-contingency states, 
when DC mode is activated.

Please note that fast mode has a few limitations:
- Contingencies applied on branches opened on one side are ignored.
- Contingencies applied on HVDC lines in AC emulation mode are ignored.
- Only PST remedial actions are supported for now.
- Slack relocation following the application of a contingency is not supported. 
As a result, security analysis is carried out only in slack component, and not necessarily in the largest one.

The default value is `false`.

## Configuration file example
See below an extract of a config file that could help:

```yaml
open-security-analysis-default-parameters:
  contingencyPropagation: true
  createResultExtension: false
  threadCount: 1
  dcFastMode: false
```

At the moment, overriding the parameters by a JSON file is not supported by Open Load Flow.

## Contingency Load Flow Parameters

A specific set of load flow parameters can be configured for each contingency individually.

These parameters correspond directly to the parameters in the [`LoadFlowParameters`](inv:powsyblcore:*:*#simulation/loadflow/configuration) from powsybl-core API and
the [`OpenLoadFlowParameters`](../loadflow/parameters.md#specific-parameters) specific parameters:
- `distributedSlack`: Refer to [`distributedSlack` in powsybl-core](inv:powsyblcore:*:*#simulation/loadflow/configuration)
- `areaInterchangeControl`: Refer to [`areaInterchangeControl` in powsybl-open-loadflow](../loadflow/parameters.md#specific-parameters)
- `balanceType`: Refer to [`balanceType` in powsybl-core](inv:powsyblcore:*:*#simulation/loadflow/configuration)

To customize these parameters for a contingency, add to the `Contingency` object a `ContingencyLoadFlowParameters` extension where you may configure the parameters.

The behaviour is not implemented yet.
It will be as follows:
- When the extension is added: The specified parameters override the corresponding SA input parameters.
- When the extension is absent: The load flow parameters provided in the SA input parameters are applied.

Note that if the operator strategies are defined for the contingency, the overridden load flow parameters will apply to
the operator strategies actions simulation too.

This extension does not override any parameter in case of a sensitivity analysis.