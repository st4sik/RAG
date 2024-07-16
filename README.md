# Retrieval Augmented Generation (RAG) with Spring AI

## Prerequisites

Before getting started with the RAG application using Spring AI, ensure you have the following prerequisites installed
on your system:

- JDK 22
- Apache Maven
- Azure OpenAI

## Application Setup

The RAG application leverages two key components for its functionality:

1. **Text Embedding (text-embedding-ada-002):**

- text-embedding-ada-002 is utilized for generating text embeddings and representing text data as vectors.

2. **Large Language Model (LLM):**

- LLM is utilized for generating answers and completing text based on learned language patterns.

### Setting up the Environment

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/st4sik/RAG.git
   ```

2. **Navigate to the Project Directory:**
   ```bash
   cd rag
   ```

3. **Build the Application:**
   ```bash
   mvn clean package
   ```

4. **Run the Application:**
   ```bash
   java -jar target/rag-spring-ai.jar
   ```

### Usage

- Use the text-embedding-ada-002 component for generating text embeddings and representing text as vectors.
- Use the LLM component for generating answers and completing text based on learned language patterns.