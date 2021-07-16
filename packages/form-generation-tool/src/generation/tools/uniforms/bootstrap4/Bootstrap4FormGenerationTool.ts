/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { UniformsFormGenerationTool } from "../UniformsFormGenerationTool";
import unescape from "lodash/unescape";
import { FormAssetType, FormAsset, FormStyle } from "../../../types";

import { renderForm } from "@kogito-tooling/uniforms-bootstrap4-codegen/dist";
import JSONSchemaBridge from "uniforms-bridge-json-schema";

export class Bootstrap4FormGenerationTool extends UniformsFormGenerationTool {
  type: string = FormStyle.BOOTSTRAP;

  protected doGenerate = (formName: string, schema: any): FormAsset => {
    const form = renderForm({
      id: formName,
      schema: new JSONSchemaBridge(schema, () => true),
      disabled: false,
      placeholder: true,
    });
    return {
      id: formName,
      assetName: `${formName}.${FormAssetType.HTML}`,
      type: FormAssetType.HTML,
      content: unescape(form),
    };
  };
}