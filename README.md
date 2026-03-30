# Subhan-Bee_Ecommerce_Order_Engine_Hackathon
"Distributed E-Commerce Order Engine - Hackathon Technical Assessment"
# Distributed E-Commerce Order Engine

## Project Overview
A robust, scalable backend engine for an e-commerce platform that handles millions of orders per day. This system simulates real-world e-commerce challenges including inventory management, concurrent user access, payment processing, order lifecycle management, and failure recovery.

## Features Implemented

### Core Features
- ✅ **Product Management** - Add, update, view products with unique IDs
- ✅ **Multi-User Cart System** - Separate carts for each user with real-time inventory sync
- ✅ **Real-Time Stock Reservation** - Prevent overselling with lock-based reservations
- ✅ **Concurrency Simulation** - Handle multiple users accessing same product
- ✅ **Order Placement Engine** - Atomic order creation with validation
- ✅ **Payment Simulation** - Random success/failure with stock restoration
- ✅ **Order Lifecycle Management** - Track orders from pending to completed/cancelled

### Advanced Features
- ✅ **Discount & Coupon Engine** - Volume discounts and coupon codes (SAVE10, FLAT200)
- ✅ **Inventory Alert System** - Low stock notifications
- ✅ **Order Cancellation Engine** - Cancel orders with stock restoration
- ✅ **Return & Refund System** - Partial returns with stock and total updates
- ✅ **Event-Driven System** - Event queue for ORDER_CREATED, PAYMENT_SUCCESS, etc.
- ✅ **Inventory Reservation Expiry** - Auto-release reserved stock after timeout
- ✅ **Audit Logging System** - Immutable logs for all actions
- ✅ **Fraud Detection System** - Flag suspicious activity (3 orders/minute, high-value)
- ✅ **Failure Injection System** - Simulate random failures for testing
- ✅ **Idempotency Handling** - Prevent duplicate order submissions
- ✅ **Microservice Simulation** - Modular architecture with loose coupling

## Design Approach

### Architecture
The system follows a clean, modular architecture with separation of concerns:
