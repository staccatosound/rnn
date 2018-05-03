import pandas as pd
import numpy as np
import tensorflow as tf
from config import DEFAULT_CONFIG

#RNN
class RNNConfig():
    input_size=1
    num_steps=30
    lstm_size=128
    num_layers=1
    keep_prob=0.8
    batch_size = 64
    window_size = 5
    init_learning_rate = 0.001
    learning_rate_decay = 0.99
    init_epoch = 5
    max_epoch = 50

def build_lstm_graph_with_config(config=None):

    tf.reset_default_graph()
    lstm_graph = tf.Graph()
    
    with lstm_graph.as_default():
        inputs = tf.placeholder("float", [None, config.num_steps, config.input_size])
        targets = tf.placeholder("float", [None, config.input_size])
        learning_rate = tf.placeholder("float", None)
    
        def _create_one_cell():
            lstm_cell = tf.contrib.rnn.LSTMCell(config.lstm_size, state_is_tuple=True)
            if config.keep_prob < 1.0:
                lstm_cell = tf.contrib.rnn.DropoutWrapper(lstm_cell, output_keep_prob=config.keep_prob)
            return lstm_cell
    
        cell = tf.contrib.rnn.MultiRNNCell(
            [_create_one_cell() for _ in range(config.num_layers)],
            state_is_tuple=True
        ) if config.num_layers > 1 else _create_one_cell()
        
        
        val, _ = tf.nn.dynamic_rnn(cell, inputs, dtype=tf.float32, scope="lilian_rnn")
    
        # Before transpose, val.get_shape() = (batch_size, num_steps, lstm_size)
        # After transpose, val.get_shape() = (num_steps, batch_size, lstm_size)
        val = tf.transpose(val, [1, 0, 2])
        
        with tf.name_scope("output_layer"):
            # last.get_shape() = (batch_size, lstm_size)
            last = tf.gather(val, int(val.get_shape()[0]) - 1, name="last_lstm_output")
        
            weight = tf.Variable(tf.truncated_normal([config.lstm_size, config.input_size]), name="lilian_weights")
            bias = tf.Variable(tf.constant(0.1, shape=[config.input_size]), name="lilian_biases")
            prediction = tf.matmul(last, weight) + bias
        
            tf.summary.histogram("last_lstm_output", last)
            tf.summary.histogram("weights", weight)
            tf.summary.histogram("biases", bias)
    
        with tf.name_scope("train"):
            # loss = -tf.reduce_sum(targets * tf.log(tf.clip_by_value(prediction, 1e-10, 1.0)))
            loss = tf.reduce_mean(tf.square(prediction - targets), name="loss_mse")
            optimizer = tf.train.AdamOptimizer(learning_rate)
            minimize = optimizer.minimize(loss, name="loss_mse_adam_minimize")
            tf.summary.scalar("loss_mse", loss)
    
        # Operators to use after restoring the model
        for op in [prediction, loss]:
            tf.add_to_collection('ops_to_restore', op)
    
#    print(1)
    return lstm_graph, loss, minimize
   
def train_lstm_graph(xs, ys, lstm_graph, loss, minimize, config=DEFAULT_CONFIG):
    with tf.Session(graph=lstm_graph) as sess:
        tf.global_variables_initializer().run()

        learning_rates_to_use = [
            config.init_learning_rate * (
                config.learning_rate_decay ** max(float(i + 1 - config.init_epoch), 0.0)
            ) for i in range(config.max_epoch)]        
            
        for epoch_step in range(config.max_epoch):
            current_lr = learning_rates_to_use[epoch_step]
     
        print("--savepoint--")
        
        x = tf.placeholder("float", [None, config.window_size], name="x")
        y_ = tf.placeholder("float", [None, 3])
#        
        num_batch = int (len(xs) / config.window_size)
        
        batch_y = []    
        batch_x = []
                  
        for b in range(num_batch):
            fr = b * config.window_size 
            to = fr + config.window_size
            m_x = xs[fr:to]
            m_x = list(m_x)
            batch_x = np.reshape(m_x, (1,5))
            print(type(batch_x[0][0]))
        
            m_y = [float(0.1) for i in range(0,3)]
            m_y[ys[b]] = float(1.1)
            m_y = list(m_y)
            batch_y = np.reshape(m_y, (1,3))
            print(type(batch_y[0][0]))
              
            temp = batch_x.tolist()[0]
            temp = temp[:3]
            temp = np.reshape(temp, (1,3))
            print(temp)
            
#            batch_x = batch_x.astype(float)
#            batch_y = batch_y.astype(float)
            print(batch_x, batch_y)            
            train_loss, _ = sess.run([loss, minimize], feed_dict={x: batch_x, y_: temp})
#                        
#            
#        saver = tf.train.Saver()
#        saver.save(sess, "your_awesome_model_path_and_name", global_step=config.max_epoch_step)
#            
    
def main(config=DEFAULT_CONFIG):    
    df = pd.read_csv('intermediate/x.csv')
    xs = list(df['x'])   
    map(float, xs)
    df = pd.read_csv('intermediate/y.csv')
    ys = list(df['y'])    
    print(xs)
    print(ys)

    config = RNNConfig()
    lstm_graph, loss, minimize = build_lstm_graph_with_config(config=config)
    train_lstm_graph(xs, ys, lstm_graph, loss, minimize, config=config)


if __name__ == '__main__':
    main()
