Problem Statement-
Small to medium organizations currently rely on manual spreadsheets or outdated, disparate systems to track valuable physical assets (like IT equipment, tools, and office fixtures). This reliance on manual processes is highly prone to human error, leads to lack of real-time visibility into asset assignment and location, and fails to systematically record essential lifecycle events like maintenance history. This results in inefficient resource management, unnecessary expenditure on replacement assets, and difficulty in accounting for physical inventory during audits.

The problem is to develop a reliable, centralized, and easy-to-use system that eliminates these manual tracking errors and provides a single, verifiable source of truth for all organizational assets.

Scope of the Project-

The scope of the AssetTrack project is limited to a core set of features necessary for small office inventory management, implemented as a monolithic Java Command Line Interface (CLI) application.
In Scope:
1.Asset Lifecycle Management: CRUD operations for Assets, Employees, and Maintenance Records.
2.Relationship Management: Linking a single asset to a single employee (One-to-Many Employee → Asset).
3.Data Persistence: Saving and loading structured data to/from a local file.
4.Non-Functional Compliance: Implementing rigorous input validation, basic role-based security simulation, and modular design.
5.Reporting: Generating contextual reports (e.g., asset history).
Out of Scope:
1.Graphical User Interface (GUI) development (e.g., using JavaFX or Swing).
2.Integration with external relational databases (e.g., MySQL, PostgreSQL).
3.Networking features (multi-user concurrent access).
Advanced security features (password hashing, user authentication).

Target Users:

1.Office Administrator / Inventory Manager (Admin Role): The primary user responsible for asset tracking, purchasing, assignment, and retirement. Needs full CRUD access, especially the ability to delete records.
2.Staff / Standard User (Staff Role): Employees who interact with the system only for viewing assets or reporting simple issues. Needs read-only access and the ability to record maintenance events.

High-Level Features:

1.Asset Inventory: Maintain a registry of all physical items.
2.Custodian Tracking: Link assets to the employee responsible for them.
3.Event Logging: Record maintenance activities and costs.
4.Data Persistence: Automatic saving and loading of all data upon application start/exit.