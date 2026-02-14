(ns codex-init
  (:require [clojure.test :refer [run-tests]]
            [blog.core :as core]
            [blog.dev-server :as dev-server])
  (:gen-class))

;; DEPRECATED: legacy Codex web integration. Do not use for Codex CLI.

(defn log-info [& messages]
  (println "[INFO]" (apply str messages)))

(defn log-error [& messages]
  (println "[ERROR]" (apply str messages)))

(defn validate-environment
  "Validates that the environment is properly set up.
   Returns exit code 0 for success, 1 for failure."
  []
  (log-info "DEPRECATED: legacy Codex web integration. Do not use for Codex CLI.")
  (log-info "Validating Org-Blog environment...")
  (try
    ;; Test basic Clojure functionality
    (assert (= 4 (+ 2 2)) "Basic Clojure functionality works")

    ;; Test that blog namespaces can be loaded
    (require 'blog.core)
    (require 'blog.dev-server)

    ;; Check for essential directories
    (assert (.exists (java.io.File. "posts")) "Posts directory exists")
    (assert (.exists (java.io.File. "src")) "Source directory exists")

    (log-info "Environment validation complete - all checks passed")
    0 ; Success exit code
    (catch Exception e
      (log-error "Environment validation failed: " (.getMessage e))
      1))) ; Error exit code

(defn run-site-generation-test
  "Tests the site generation functionality.
   Returns exit code 0 for success, 1 for failure."
  []
  (log-info "Testing site generation...")
  (try
    (core/regenerate-site)
    (log-info "Site generation test passed")
    0 ; Success exit code
    (catch Exception e
      (log-error "Site generation test failed: " (.getMessage e))
      1))) ; Error exit code

(defn start-dev-environment
  "Starts the development environment.
   Returns exit code 0 for success, 1 for failure."
  []
  (log-info "Starting development environment...")
  (try
    (dev-server/start-server)
    (log-info "Development server started on port 8081")
    ; Note: This will keep running until interrupted
    0 ; Success exit code
    (catch Exception e
      (log-error "Failed to start development server: " (.getMessage e))
      1))) ; Error exit code

(defn initialize-codex-environment
  "Main entry point for Codex initialization.
   Returns exit code 0 for success, 1 for failure."
  []
  (log-info "=== Org-Blog Codex Environment Initialization ===")
  (let [exit-code (validate-environment)]
    (if (zero? exit-code)
      (do
        (log-info "Environment is ready for Codex agent operations")
        (log-info "Available commands:")
        (log-info "clojure -e \"(require 'codex-init) (codex-init/validate-environment)\"")
        (log-info "clojure -e \"(require 'codex-init) (codex-init/run-site-generation-test)\"")
        (log-info "clojure -e \"(require 'codex-init) (codex-init/start-dev-environment)\"")
        0) ; Success exit code
      exit-code))) ; Return the validation error code

;; Command-line entry point
(defn -main [& args]
  (let [command (first args)
        exit-code (case command
                    "validate" (validate-environment)
                    "generate" (run-site-generation-test)
                    "serve" (start-dev-environment)
                    (do
                      (log-error "Unknown command: " command)
                      (log-info "Available commands: validate, generate, serve")
                      1))]
    (System/exit exit-code)))

;; Run initialization when loaded via require, but don't exit
(when *compile-files*
  (initialize-codex-environment))
