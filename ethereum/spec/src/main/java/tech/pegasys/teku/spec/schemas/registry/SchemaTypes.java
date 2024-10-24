/*
 * Copyright Consensys Software Inc., 2024
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package tech.pegasys.teku.spec.schemas.registry;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.base.MoreObjects;
import java.util.Locale;
import tech.pegasys.teku.infrastructure.ssz.collections.SszBitvector;
import tech.pegasys.teku.infrastructure.ssz.schema.collections.SszBitvectorSchema;
import tech.pegasys.teku.spec.SpecMilestone;
import tech.pegasys.teku.spec.datastructures.execution.ExecutionPayloadHeader;
import tech.pegasys.teku.spec.datastructures.execution.ExecutionPayloadHeaderSchema;
import tech.pegasys.teku.spec.datastructures.networking.libp2p.rpc.BeaconBlocksByRootRequestMessage.BeaconBlocksByRootRequestMessageSchema;
import tech.pegasys.teku.spec.datastructures.operations.AggregateAndProof.AggregateAndProofSchema;
import tech.pegasys.teku.spec.datastructures.operations.Attestation;
import tech.pegasys.teku.spec.datastructures.operations.AttestationSchema;
import tech.pegasys.teku.spec.datastructures.operations.AttesterSlashingSchema;
import tech.pegasys.teku.spec.datastructures.operations.IndexedAttestationSchema;
import tech.pegasys.teku.spec.datastructures.operations.SignedAggregateAndProof.SignedAggregateAndProofSchema;
import tech.pegasys.teku.spec.datastructures.state.HistoricalBatch.HistoricalBatchSchema;
import tech.pegasys.teku.spec.datastructures.state.beaconstate.BeaconState;
import tech.pegasys.teku.spec.datastructures.state.beaconstate.BeaconStateSchema;
import tech.pegasys.teku.spec.datastructures.state.beaconstate.MutableBeaconState;

public class SchemaTypes {
  // PHASE0
  public static final SchemaId<SszBitvectorSchema<SszBitvector>> ATTNETS_ENR_FIELD_SCHEMA =
      create("ATTNETS_ENR_FIELD_SCHEMA");
  public static final SchemaId<SszBitvectorSchema<SszBitvector>> SYNCNETS_ENR_FIELD_SCHEMA =
      create("SYNCNETS_ENR_FIELD_SCHEMA");
  public static final SchemaId<HistoricalBatchSchema> HISTORICAL_BATCH_SCHEMA =
      create("HISTORICAL_BATCH_SCHEMA");
  public static final SchemaId<BeaconBlocksByRootRequestMessageSchema>
      BEACON_BLOCKS_BY_ROOT_REQUEST_MESSAGE_SCHEMA =
          create("BEACON_BLOCKS_BY_ROOT_REQUEST_MESSAGE_SCHEMA");
  public static final SchemaId<AttesterSlashingSchema> ATTESTER_SLASHING_SCHEMA =
      create("ATTESTER_SLASHING_SCHEMA");
  public static final SchemaId<IndexedAttestationSchema> INDEXED_ATTESTATION_SCHEMA =
      create("INDEXED_ATTESTATION_SCHEMA");

  public static final SchemaId<AttestationSchema<Attestation>> ATTESTATION_SCHEMA =
      create("ATTESTATION_SCHEMA");

  public static final SchemaId<AggregateAndProofSchema> AGGREGATE_AND_PROOF_SCHEMA =
      create("AGGREGATE_AND_PROOF_SCHEMA");
  public static final SchemaId<SignedAggregateAndProofSchema> SIGNED_AGGREGATE_AND_PROOF_SCHEMA =
      create("SIGNED_AGGREGATE_AND_PROOF_SCHEMA");

  public static final SchemaId<ExecutionPayloadHeaderSchema<? extends ExecutionPayloadHeader>>
      EXECUTION_PAYLOAD_HEADER_SCHEMA = create("EXECUTION_PAYLOAD_HEADER_SCHEMA");

  public static final SchemaId<BeaconStateSchema<BeaconState, MutableBeaconState>>
      BEACON_STATE_SCHEMA = create("BEACON_STATE_SCHEMA");

  // Altair

  // Bellatrix

  // Capella

  // Deneb

  private SchemaTypes() {
    // Prevent instantiation
  }

  @VisibleForTesting
  static <T> SchemaId<T> create(final String name) {
    return new SchemaId<>(name);
  }

  public static class SchemaId<T> {
    private static final Converter<String, String> UPPER_UNDERSCORE_TO_UPPER_CAMEL =
        CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.UPPER_CAMEL);

    public static String upperSnakeCaseToUpperCamel(final String camelCase) {
      return UPPER_UNDERSCORE_TO_UPPER_CAMEL.convert(camelCase);
    }

    private static String capitalizeMilestone(final SpecMilestone milestone) {
      return milestone.name().charAt(0) + milestone.name().substring(1).toLowerCase(Locale.ROOT);
    }

    private final String name;

    private SchemaId(final String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public String getContainerName(final SpecMilestone milestone) {
      return getContainerName() + capitalizeMilestone(milestone);
    }

    public String getContainerName() {
      return upperSnakeCaseToUpperCamel(name.replace("_SCHEMA", ""));
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o instanceof SchemaId<?> other) {
        return name.equals(other.name);
      }
      return false;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this).add("name", name).toString();
    }
  }
}
