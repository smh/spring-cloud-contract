/*
 * Copyright 2020-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.contract.verifier.file;

import java.io.File;

import org.junit.jupiter.api.Test;

import org.springframework.cloud.contract.spec.Contract;

import static org.assertj.core.api.BDDAssertions.then;

class ContractMetadataTests {

	@Test
	void should_return_false_when_http_does_not_have_from_file_payload() {
		ContractMetadata metadata = new ContractMetadata(new File(".").toPath(), false, 1,
				0, Contract.make(c -> {
					c.request(r -> {
						r.url("/foo");
						r.method("GET");
					});
					c.response(r -> {
						r.status(200);
					});
				}));

		then(metadata.anyPayloadFromFile()).isFalse();
	}

	@Test
	void should_return_false_when_messaging_does_not_have_from_file_payload() {
		ContractMetadata metadata = new ContractMetadata(new File(".").toPath(), false, 1,
				0, Contract.make(c -> {
					c.input(input -> {
						input.triggeredBy("foo");
					});
					c.outputMessage(outputMessage -> {
						outputMessage.body("foo");
					});
				}));

		then(metadata.anyPayloadFromFile()).isFalse();
	}

	@Test
	void should_return_true_when_request_has_from_file_payload() {
		ContractMetadata metadata = new ContractMetadata(new File(".").toPath(), false, 1,
				0, Contract.make(c -> {
					c.request(r -> {
						r.url("/foo");
						r.method("GET");
						r.body(r.file("main.json"));
					});
					c.response(r -> {
						r.status(200);
					});
				}));

		then(metadata.anyPayloadFromFile()).isTrue();
	}

	@Test
	void should_return_true_when_response_has_from_file_payload() {
		ContractMetadata metadata = new ContractMetadata(new File(".").toPath(), false, 1,
				0, Contract.make(c -> {
					c.request(r -> {
						r.url("/foo");
						r.method("GET");
					});
					c.response(r -> {
						r.status(200);
						r.body(r.file("main.json"));
					});
				}));

		then(metadata.anyPayloadFromFile()).isTrue();
	}

	@Test
	void should_return_true_when_input_msg_has_from_file_payload() {
		ContractMetadata metadata = new ContractMetadata(new File(".").toPath(), false, 1,
				0, Contract.make(c -> {
					c.input(i -> {
						i.messageBody(i.file("main.json"));
					});
				}));

		then(metadata.anyPayloadFromFile()).isTrue();
	}

	@Test
	void should_return_true_when_output_msg_has_from_file_payload() {
		ContractMetadata metadata = new ContractMetadata(new File(".").toPath(), false, 1,
				0, Contract.make(c -> {
					c.outputMessage(o -> {
						o.body(o.file("main.json"));
					});
				}));

		then(metadata.anyPayloadFromFile()).isTrue();
	}

}
