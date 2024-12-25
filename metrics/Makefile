SHELL=bash
UNAME := $(shell uname)
PURPOSE:=metrics
SERVICE_NAME:=custom-metrics-apiserver
ALT_NAMES:="custom-metrics-apiserver.monitoring","custom-metrics-apiserver.monitoring.svc"
SECRET_FILE:=custom-metrics-api/cm-adapter-serving-certs.yaml
OUTPUT_DIR:=output
SEC_DIR:=custom-metrics-api

# Targets
certs: gensecret rmcerts
.PHONY: gencerts
gencerts:
	@echo Generating TLS certs
	@mkdir -p $(OUTPUT_DIR)
	@mkdir -p $(SEC_DIR)
	@openssl genpkey -algorithm RSA -out $(OUTPUT_DIR)/$(PURPOSE)-ca.key -pkeyopt rsa_keygen_bits:2048
	@openssl req -x509 -sha256 -new -nodes -key $(OUTPUT_DIR)/$(PURPOSE)-ca.key -out $(OUTPUT_DIR)/$(PURPOSE)-ca.crt -subj "/CN=ca" -days 365
	@openssl genpkey -algorithm RSA -out $(OUTPUT_DIR)/apiserver-key.pem -pkeyopt rsa_keygen_bits:2048
	@openssl req -new -key $(OUTPUT_DIR)/apiserver-key.pem -out $(OUTPUT_DIR)/apiserver.csr -subj "/CN=$(SERVICE_NAME)"
	@openssl x509 -req -sha256 -in $(OUTPUT_DIR)/apiserver.csr -CA $(OUTPUT_DIR)/$(PURPOSE)-ca.crt -CAkey $(OUTPUT_DIR)/$(PURPOSE)-ca.key -CAcreateserial -out $(OUTPUT_DIR)/apiserver.pem -days 365

.PHONY: gensecret
gensecret: gencerts
	@echo Generating $(SECRET_FILE)
	@echo "apiVersion: v1" > $(SECRET_FILE)
	@echo "kind: Secret" >> $(SECRET_FILE)
	@echo "metadata:" >> $(SECRET_FILE)
	@echo "  name: cm-adapter-serving-certs" >> $(SECRET_FILE)
	@echo "  namespace: monitoring" >> $(SECRET_FILE)
	@echo "data:" >> $(SECRET_FILE)
ifeq ($(UNAME), Darwin)
	@echo "  serving.crt: $$(cat $(OUTPUT_DIR)/apiserver.pem | base64)" >> $(SECRET_FILE)
	@echo "  serving.key: $$(cat $(OUTPUT_DIR)/apiserver-key.pem | base64)" >> $(SECRET_FILE)
endif
ifeq ($(UNAME), Linux)
	@echo "  serving.crt: $$(cat $(OUTPUT_DIR)/apiserver.pem | base64 -w 0)" >> $(SECRET_FILE)
	@echo "  serving.key: $$(cat $(OUTPUT_DIR)/apiserver-key.pem | base64 -w 0)" >> $(SECRET_FILE)
endif

.PHONY: rmcerts
rmcerts:
	@echo removing files
	@rm -f $(OUTPUT_DIR)/apiserver-key.pem $(OUTPUT_DIR)/apiserver.csr $(OUTPUT_DIR)/apiserver.pem
	@rm -f $(OUTPUT_DIR)/$(PURPOSE)-ca.crt $(OUTPUT_DIR)/$(PURPOSE)-ca.key