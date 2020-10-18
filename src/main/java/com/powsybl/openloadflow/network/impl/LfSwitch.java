/**
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.powsybl.openloadflow.network.impl;

import com.powsybl.iidm.network.Switch;
import com.powsybl.openloadflow.network.*;
import com.powsybl.openloadflow.util.Evaluable;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Geoffroy Jamgotchian <geoffroy.jamgotchian at rte-france.com>
 */
public class LfSwitch extends AbstractLfBranch {

    private final Switch aSwitch;

    public LfSwitch(LfBus bus1, LfBus bus2, Switch aSwitch) {
        super(bus1, bus2, new SimplePiModel());
        this.aSwitch = Objects.requireNonNull(aSwitch);
    }

    @Override
    public String getId() {
        return aSwitch.getId();
    }

    @Override
    public void setP1(Evaluable p1) {
        // nothing to do
    }

    @Override
    public void setP2(Evaluable p2) {
        // nothing to do
    }

    @Override
    public void setQ1(Evaluable q1) {
        // nothing to do
    }

    @Override
    public void setQ2(Evaluable q2) {
        // nothing to do
    }

    @Override
    public double getI1() {
        return Double.NaN;
    }

    @Override
    public double getI2() {
        return Double.NaN;
    }

    @Override
    public double getPermanentLimit1() {
        return Double.NaN;
    }

    @Override
    public double getPermanentLimit2() {
        return Double.NaN;
    }

    @Override
    public Optional<PhaseControl> getPhaseControl() {
        return Optional.empty();
    }

    @Override
    public void updateState() {
        // nothing to do
    }
}