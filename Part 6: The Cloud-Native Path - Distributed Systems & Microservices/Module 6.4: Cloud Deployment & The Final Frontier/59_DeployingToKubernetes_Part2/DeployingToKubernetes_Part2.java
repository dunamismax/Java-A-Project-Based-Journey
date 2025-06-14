
/**
 * This lesson is a hands-on guide to deploying our containerized Spring Boot
 * application to a **Kubernetes (K8s)** cluster.
 *
 * We will take the Docker image we created in a previous lesson and write the
 * necessary Kubernetes **YAML manifest files** to describe our desired state.
 * This is how modern applications are deployed in the cloud.
 *
 * ## The Goal
 *
 * We will create two core Kubernetes objects:
 * 1.  A **Deployment**: To tell Kubernetes to run two replicas (instances) of our
 *     application Pod for high availability.
 * 2.  A **Service**: To expose our Deployment to network traffic, providing a single,
 *     stable endpoint to access our application. We'll use a `LoadBalancer` type
 *     service to make it accessible from outside the cluster.
 *
 * ## PREQUISITES:
 *
 * 1.  **A Kubernetes Cluster:**
 *     - **For local development**, the easiest option is to enable Kubernetes within
 *       **Docker Desktop** (Settings -> Kubernetes -> Enable Kubernetes).
 *     - Alternatively, you can install tools like **Minikube** or **kind**.
 *
 * 2.  **`kubectl` (Kubernetes Command-Line Tool):**
 *     - This is the primary tool for interacting with your cluster. It is automatically
 *       installed with Docker Desktop's Kubernetes engine.
 *     - Verify your setup by running `kubectl cluster-info`.
 *
 * 3.  **A Docker Image:**
 *     - You must have a Docker image of your application built and available. For local
 *       testing, the image built on your machine is fine. For a real cloud deployment,
 *       you would push this image to a container registry like Docker Hub, Google (GCR),
 *       or Amazon (ECR).
 *
 * ## SETUP: Create YAML Manifest Files
 *
 * In the root of your project, create a new directory called `k8s`. Inside it, create
 * the following two files.
 *
 * **File: `k8s/deployment.yml`**
 * ```yaml
 * apiVersion: apps/v1
 * kind: Deployment
 * metadata:
 *   name: my-java-app-deployment
 * spec:
 *   replicas: 2 # Tell Kubernetes we want 2 instances of our app running.
 *   selector:
 *     matchLabels:
 *       app: my-java-app
 *   template:
 *     metadata:
 *       labels:
 *         app: my-java-app # This label connects the Pods to the Deployment.
 *     spec:
 *       containers:
 *         - name: my-java-app-container
 *           image: my-java-app:latest # The name and tag of your Docker image.
 *           imagePullPolicy: IfNotPresent # Important for using local images.
 *           ports:
 *             - containerPort: 8080 # The port your Spring Boot app runs on.
 * ```
 *
 * **File: `k8s/service.yml`**
 * ```yaml
 * apiVersion: v1
 * kind: Service
 * metadata:
 *   name: my-java-app-service
 * spec:
 *   selector:
 *     app: my-java-app # This selector links the Service to the Pods with the matching label.
 *   ports:
 *     - protocol: TCP
 *       port: 80 # The port the Service will expose.
 *       targetPort: 8080 # The port on the Pods to forward traffic to.
 *   type: LoadBalancer # Makes the service accessible from outside the cluster.
 *                     # On local clusters, this usually uses localhost. In the cloud,
 *                     # it provisions a real load balancer.
 * ```
 *
 * ## HOW TO DEPLOY AND TEST:
 * 1.  **Open a terminal** in the root directory of your project.
 *
 * 2.  **Apply the manifests to your cluster:**
 *     The `apply -f` command tells `kubectl` to create or update the resources
 *     defined in the specified file or directory.
 *     ```sh
 *     kubectl apply -f k8s/
 *     ```
 *
 * 3.  **Check the status of your deployment:**
 *     It may take a minute for the Pods to download the image and start.
 *     ```sh
 *     kubectl get deployment my-java-app-deployment
 *     # Wait until the 'AVAILABLE' column shows 2.
 *     ```
 *
 * 4.  **Check the status of your Pods:**
 *     ```sh
 *     kubectl get pods
 *     # You should see two Pods with names like 'my-java-app-deployment-...'
 *     ```
 *
 * 5.  **Check the status of your Service:**
 *     ```sh
 *     kubectl get service my-java-app-service
 *     # Look for the EXTERNAL-IP. For Docker Desktop, it will be 'localhost'.
 *     ```
 *
 * 6.  **Test your deployed application!**
 *     The Service is listening on port 80. Open your browser or use `curl`:
 *     ```sh
 *     curl http://localhost
 *     ```
 *     If you run the command multiple times, you may see responses from different
 *     Pod hostnames, proving the load balancing is working!
 *
 * 7.  **To clean up**, delete the resources from your cluster:
 *     ```sh
 *     kubectl delete -f k8s/
 *     ```
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// --- Main Application Entry Point ---
// This is the same simple application we containerized in Lesson 45.
// The focus of this lesson is on the Kubernetes YAML files, not the Java code.
@SpringBootApplication
public class DeployingToKubernetes_Part2 {
    public static void main(String[] args) {
        SpringApplication.run(DeployingToKubernetes_Part2.class, args);
    }
}

@RestController
class KubernetesController {
    @GetMapping("/")
    public String getK8sGreeting() {
        // Get the Pod's hostname from the environment variables.
        String podName = System.getenv("HOSTNAME");
        return "Hello from inside a Kubernetes Pod! My Pod name is: " + podName;
    }
}