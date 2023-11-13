/*
 * Copyright Consensys Software Inc., 2022
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

package tech.pegasys.teku.statetransition.blobs;

import java.util.List;
import java.util.Optional;
import tech.pegasys.teku.infrastructure.async.SafeFuture;
import tech.pegasys.teku.infrastructure.unsigned.UInt64;
import tech.pegasys.teku.spec.datastructures.blobs.versions.deneb.BlobSidecar;
import tech.pegasys.teku.spec.datastructures.blobs.versions.deneb.BlobSidecarOld;
import tech.pegasys.teku.spec.datastructures.blobs.versions.deneb.SignedBlobSidecarOld;
import tech.pegasys.teku.spec.datastructures.blocks.SignedBeaconBlock;
import tech.pegasys.teku.spec.logic.versions.deneb.blobs.BlobSidecarsAndValidationResult;
import tech.pegasys.teku.spec.logic.versions.deneb.blobs.BlobSidecarsAvailabilityChecker;
import tech.pegasys.teku.statetransition.validation.InternalValidationResult;

public interface BlobSidecarManager {
  BlobSidecarManager NOOP =
      new BlobSidecarManager() {

        @Override
        public SafeFuture<InternalValidationResult> validateAndPrepareForBlockImport(
            final SignedBlobSidecarOld signedBlobSidecar, final Optional<UInt64> arrivalTimestamp) {
          return SafeFuture.completedFuture(InternalValidationResult.ACCEPT);
        }

        @Override
        public SafeFuture<InternalValidationResult> validateAndPrepareForBlockImport(
            final BlobSidecar blobSidecar, final Optional<UInt64> arrivalTimestamp) {
          return SafeFuture.completedFuture(InternalValidationResult.ACCEPT);
        }

        @Override
        public void prepareForBlockImport(final BlobSidecarOld blobSidecar) {}

        @Override
        public void subscribeToReceivedBlobSidecar(
            final ReceivedBlobSidecarListener receivedBlobSidecarListener) {}

        @Override
        public boolean isAvailabilityRequiredAtSlot(final UInt64 slot) {
          return false;
        }

        @Override
        public BlobSidecarsAvailabilityChecker createAvailabilityChecker(
            final SignedBeaconBlock block) {
          return BlobSidecarsAvailabilityChecker.NOOP;
        }

        @Override
        public BlobSidecarsAndValidationResult createAvailabilityCheckerAndValidateImmediately(
            final SignedBeaconBlock block, final List<BlobSidecarOld> blobSidecars) {
          return BlobSidecarsAndValidationResult.NOT_REQUIRED;
        }
      };

  @Deprecated
  SafeFuture<InternalValidationResult> validateAndPrepareForBlockImport(
      SignedBlobSidecarOld signedBlobSidecar, Optional<UInt64> arrivalTimestamp);

  SafeFuture<InternalValidationResult> validateAndPrepareForBlockImport(
      BlobSidecar blobSidecar, Optional<UInt64> arrivalTimestamp);

  void prepareForBlockImport(BlobSidecarOld blobSidecar);

  void subscribeToReceivedBlobSidecar(ReceivedBlobSidecarListener receivedBlobSidecarListener);

  boolean isAvailabilityRequiredAtSlot(UInt64 slot);

  BlobSidecarsAvailabilityChecker createAvailabilityChecker(SignedBeaconBlock block);

  BlobSidecarsAndValidationResult createAvailabilityCheckerAndValidateImmediately(
      SignedBeaconBlock block, List<BlobSidecarOld> blobSidecars);

  interface ReceivedBlobSidecarListener {
    void onBlobSidecarReceived(BlobSidecarOld blobSidecar);
  }
}
