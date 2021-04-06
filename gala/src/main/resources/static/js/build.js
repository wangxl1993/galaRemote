"use strict";
console.clear();
	
const webGL = (transforms, setup) => {
	const canvas = document.querySelector("canvas");
	const gl = canvas.getContext("webgl", { alpha: false });
	if (!gl) return false;
	gl.enable(gl.DEPTH_TEST);
	gl.enable(gl.CULL_FACE);

	const resize = () => {
		canvas.width = canvas.offsetWidth;
		canvas.height = canvas.offsetHeight;
		gl.viewport(0, 0, gl.drawingBufferWidth, gl.drawingBufferHeight);
		camera.projection(fov);
	};

	window.addEventListener("resize", () => {
		mustResize = true;
	});

	const clearScreen = () => {
		if (mustResize) resize();
		gl.clearColor(0.8, 0.9, 1, 1);
		gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
	};

	const u3 = (program, name) => {
		const loc = gl.getUniformLocation(program, name);
		return {
			set (v) {
				gl.uniform3f(loc, v[0], v[1], v[2]);
			}
		}
	};

	const computeNormals = (v) => {
		const n = [];
		for (let i = 0; i < v.length; i += 9) {
			const p1x = v[i + 3] - v[i];
			const p1y = v[i + 4] - v[i + 1];
			const p1z = v[i + 5] - v[i + 2];
			const p2x = v[i + 6] - v[i];
			const p2y = v[i + 7] - v[i + 1];
			const p2z = v[i + 8] - v[i + 2];
			const p3x = p1y * p2z - p1z * p2y;
			const p3y = -(p1x * p2z - p1z * p2x);
			const p3z = p1x * p2y - p1y * p2x;
			const mag = Math.sqrt(p3x * p3x + p3y * p3y + p3z * p3z);
			if (mag === 0) {
				n.push(0, 0, 0, 0, 0, 0, 0, 0, 0);
			} else {
				n.push(p3x / mag, p3y / mag, p3z / mag);
				n.push(p3x / mag, p3y / mag, p3z / mag);
				n.push(p3x / mag, p3y / mag, p3z / mag);
			}
		}
		return n;
	};

	const Camera = () => {
		const camProj = gl.getUniformLocation(program, "camProj");
		const mView = new Float32Array(16);
		const mProj = new Float32Array(16);
		const camView = gl.getUniformLocation(program, "camView");
		return {
			dist: -setup.camDist,
			move(rx, ry, z) {
				mView.set([1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, z, 1]);
				if (rx !== 0) transforms.rx(mView, rx);
				if (ry !== 0) transforms.ry(mView, ry);
				gl.uniformMatrix4fv(camView, false, mView);
			},
			projection(fov) {
				const near = 0.001;
				const far = 100;
				const top = near * Math.tan((fov * Math.PI) / 360);
				const right = top * (gl.drawingBufferWidth / gl.drawingBufferHeight);
				const left = -right;
				const bottom = -top;
				mProj[0] = (2 * near) / (right - left);
				mProj[5] = (2 * near) / (top - bottom);
				mProj[8] = (right + left) / (right - left);
				mProj[9] = (top + bottom) / (top - bottom);
				mProj[10] = -(far + near) / (far - near);
				mProj[11] = -1;
				mProj[14] = -(2 * far * near) / (far - near);
				gl.uniformMatrix4fv(camProj, false, mProj);
			}
		};
	};

	const buffer = (program, attributeName, size, type) => {
		const buffer = gl.createBuffer();
		const loc = gl.getAttribLocation(program, attributeName);
		gl.enableVertexAttribArray(loc);
		return {
			load (array) {
				gl.bindBuffer(gl.ARRAY_BUFFER, buffer);
				gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(array), type);
				gl.vertexAttribPointer(loc, size, gl.FLOAT, false, 0, 0);
			}
		}
	};

	const createShader = (source, type) => {
		const shader = gl.createShader(type);
		gl.shaderSource(shader, source);
		gl.compileShader(shader);
		if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
			throw new Error(gl.getShaderInfoLog(shader));
		}
		return shader;
	}

	const compileShaders = (vertex, fragment) => {
		const vertexShader = createShader(vertex, gl.VERTEX_SHADER);
		const fragmentShader = createShader(fragment, gl.FRAGMENT_SHADER);
		const program = gl.createProgram();
		gl.attachShader(program, vertexShader);
		gl.attachShader(program, fragmentShader);
		gl.linkProgram(program);
		gl.useProgram(program);
		return program;
	};

	// ---- shaders -----

	const vertex = `
		precision highp float;
		uniform mat4 camProj, camView;
		attribute vec3 aPosition, aNormal;
		attribute float aBrillance;
		varying vec4 vPosition;
		varying vec3 vNormal, vColor;
		void main() {
			vPosition = camView * vec4(aPosition, 1.0);
			gl_Position = camProj * vPosition;
			vNormal = normalize(vec3(camView * vec4(aNormal, 0.0)));
			vColor = vec3(aBrillance, aBrillance, aBrillance);
		}`;

	const fragment = `
		precision highp float;
		varying vec4 vPosition;
		varying vec3 vNormal, vColor;
		uniform vec3 uLightPosition, uAmbientColor;
		uniform vec3 uSpecularColor, uDiffuseColor;
		const float density = 1.0; // fog density
		void main() {
			float z = gl_FragCoord.z / gl_FragCoord.w;
			float fogFactor = clamp(exp2( - density * density * pow(z, 3.0)), 0.0, 1.0);
			vec3 lightDirection = normalize(uLightPosition - vPosition.xyz);
			vec3 eyeDirection = normalize(-vPosition.xyz);
			vec3 reflectionDirection = reflect(-lightDirection, vNormal);
			float specularLightWeighting = pow(max(dot(reflectionDirection, eyeDirection), 0.0), 1.5); // shininess
			float diffuseLightWeighting = max(dot(vNormal, lightDirection), 0.0);
			vec3 lightWeighting = uAmbientColor
			+ uSpecularColor * specularLightWeighting
			+ uDiffuseColor * diffuseLightWeighting;
			vec3 color = vColor * lightWeighting;
			gl_FragColor = mix(vec4(0.8, 0.9, 1.0, 1.0), vec4(color, 1.0), fogFactor);
		}`;

	const program = compileShaders(vertex, fragment);
	const aPosition = buffer(program, "aPosition", 3, gl.STATIC_DRAW);
	const aNormal = buffer(program, "aNormal", 3, gl.STATIC_DRAW);
	const aBrillance = buffer(program, "aBrillance", 1, gl.STATIC_DRAW);

	u3(program, "uLightPosition").set(setup.lightPosition);
	u3(program, "uAmbientColor").set(setup.ambientColor);
	u3(program, "uSpecularColor").set(setup.specularColor);
	u3(program, "uDiffuseColor").set(setup.diffuseColor);

	const fov = setup.fov;
	let ry = 0;
	let nVertices = 0;
	let mustResize = true;
	const camera = Camera();

	const loadVertices = (vertices, brillance, rx, cz,) => {
		const normals = computeNormals(vertices);
		nVertices = vertices.length / 3;
		aPosition.load(vertices);
		aNormal.load(normals);
		aBrillance.load(brillance);
		camera.rx = rx;
		camera.cz = cz;
	};

	const anim = () => {
		requestAnimationFrame(anim);
		clearScreen();
		camera.move(camera.rx, ry, camera.dist + camera.cz);
		ry -= 0.1;
		gl.drawArrays(gl.TRIANGLES, 0, nVertices);
		gl.drawArrays(gl.LINE_LOOP, 0, nVertices);
	};

	return {
		gl: gl,
		loadVertices: loadVertices,
		resize: resize,
		anim: anim
	};
};

///////////////////////////////////////////////////////

const structure = (setup, rules) => {

	const shapes = [];
	const stack = [];
	const vertices = [];
	const brillance = [];
	let seed = 0;

	const transforms = {
		x(m, v) {
			m[12] += m[0] * v;
			m[13] += m[1] * v;
			m[14] += m[2] * v;
			m[15] += m[3] * v;
		},
		y(m, v) {
			m[12] += m[4] * v;
			m[13] += m[5] * v;
			m[14] += m[6] * v;
			m[15] += m[7] * v;
		},
		z(m, v) {
			m[12] += m[8] * v;
			m[13] += m[9] * v;
			m[14] += m[10] * v;
			m[15] += m[11] * v;
		},
		s(m, v) {
			const a = Array.isArray(v);
			const x = a ? v[0] : v;
			const y = a ? v[1] : x;
			const z = a ? v[2] : x;
			m[0] *= x;
			m[1] *= x;
			m[2] *= x;
			m[3] *= x;
			m[4] *= y;
			m[5] *= y;
			m[6] *= y;
			m[7] *= y;
			m[8] *= z;
			m[9] *= z;
			m[10] *= z;
			m[11] *= z;
		},
		rx(m, v) {
			const rad = Math.PI * (v / 180);
			const s = Math.sin(rad);
			const c = Math.cos(rad);
			const a10 = m[4];
			const a11 = m[5];
			const a12 = m[6];
			const a13 = m[7];
			const a20 = m[8];
			const a21 = m[9];
			const a22 = m[10];
			const a23 = m[11];
			m[4] = a10 * c + a20 * s;
			m[5] = a11 * c + a21 * s;
			m[6] = a12 * c + a22 * s;
			m[7] = a13 * c + a23 * s;
			m[8] = a10 * -s + a20 * c;
			m[9] = a11 * -s + a21 * c;
			m[10] = a12 * -s + a22 * c;
			m[11] = a13 * -s + a23 * c;
		},
		ry(m, v) {
			const rad = Math.PI * (v / 180);
			const s = Math.sin(rad);
			const c = Math.cos(rad);
			const a00 = m[0];
			const a01 = m[1];
			const a02 = m[2];
			const a03 = m[3];
			const a20 = m[8];
			const a21 = m[9];
			const a22 = m[10];
			const a23 = m[11];
			m[0] = a00 * c + a20 * -s;
			m[1] = a01 * c + a21 * -s;
			m[2] = a02 * c + a22 * -s;
			m[3] = a03 * c + a23 * -s;
			m[8] = a00 * s + a20 * c;
			m[9] = a01 * s + a21 * c;
			m[10] = a02 * s + a22 * c;
			m[11] = a03 * s + a23 * c;
		},
		rz(m, v) {
			const rad = Math.PI * (v / 180);
			const s = Math.sin(rad);
			const c = Math.cos(rad);
			const a00 = m[0];
			const a01 = m[1];
			const a02 = m[2];
			const a03 = m[3];
			const a10 = m[4];
			const a11 = m[5];
			const a12 = m[6];
			const a13 = m[7];
			m[0] = a00 * c + a10 * s;
			m[1] = a01 * c + a11 * s;
			m[2] = a02 * c + a12 * s;
			m[3] = a03 * c + a13 * s;
			m[4] = a00 * -s + a10 * c;
			m[5] = a01 * -s + a11 * c;
			m[6] = a02 * -s + a12 * c;
			m[7] = a03 * -s + a13 * c;
		},
		b(m, v) {
			if (v > 0) {
				m[16] += v * (1 - m[16]);
			} else {
				m[16] += v * m[16];
			}
		}
	};

	const genCube = (x, y, z) => {
		return [
			[-x, -y, -z], [-x,  y, -z], [ x,  y, -z], [ x, -y, -z], [-x, -y, -z], [ x,  y, -z],
			[ x,  y,  z], [-x,  y,  z], [-x, -y,  z], [ x,  y,  z], [-x, -y,  z], [ x, -y,  z],
			[-x,  y, -z], [-x,  y,  z], [ x,  y,  z], [ x,  y, -z], [-x,  y, -z], [ x,  y,  z],
			[ x, -y,  z], [-x, -y,  z], [-x, -y, -z], [ x, -y,  z], [-x, -y, -z], [ x, -y, -z],
			[-x, -y, -z], [-x, -y,  z], [-x,  y,  z], [-x,  y, -z], [-x, -y, -z], [-x,  y,  z],
			[ x,  y,  z], [ x, -y,  z], [ x, -y, -z], [ x,  y,  z], [ x, -y, -z], [ x,  y, -z]
		];
	};

	const vCube = genCube(0.5, 0.5, 0.5);

	const webgl = webGL(transforms, setup);

	const pushVertices = (v, m) => {
		const x = v[0];
		const y = v[1];
		const z = v[2];
		const w = m[3] * x + m[7] * y + m[11] * z + m[15] || 1.0;
		vertices.push(
			(m[0] * x + m[4] * y + m[8] * z + m[12]) / w,
			(m[1] * x + m[5] * y + m[9] * z + m[13]) / w,
			(m[2] * x + m[6] * y + m[10] * z + m[14]) / w
		);
		brillance.push(m[16]);
	};

	const cube = (m, t) => {
		const s = copy(m);
		for (const c in t) {
			if (transforms[c]) transforms[c](s, t[c]);
			else console.log("error: " + c);
		}
		for (const v of vCube) {
			pushVertices(v, s);
		}
	};
	
	const size = (m) => {
		return Math.min(
			m[0] * m[0] + m[1] * m[1] + m[2] * m[2],
			m[4] * m[4] + m[5] * m[5] + m[6] * m[6],
			m[8] * m[8] + m[9] * m[9] + m[10] * m[10]
		);
	};

	const transform = (s, p) => {
		const m = copy(s);
		m[18]++;
		for (const c in p) {
			if (transforms[c]) transforms[c](m, p[c]);
			else console.log("error: " + c);
		}
		if (minSize === 0) return m;
		else {
			if (size(m) < minSize) m[17] = -1;
			return m;
		}
	};

	const random = (_) => {
		seed = (seed * 16807) % 2147483647;
		return (seed - 1) / 2147483646;
	};

	const copy = (s) => {
		return [
			// 4 x 4 transform matrix
			s[0], s[1], s[2], s[3], 
			s[4], s[5], s[6], s[7], 
			s[8], s[9], s[10], s[11], 
			s[12], s[13], s[14], s[15], 
			s[16], // brillance
			s[17], // not used
			s[18], // recurcivity depth
			s[19]  // function index
		];
	};

	const newStructure = () => {
		console.log("seed:", seed);
		vertices.length = 0;
		brillance.length = 0;
		runshapes(setup.start, setup.transform || {});
		const rx = random() * 60 - 30;
		const cz = random() * 0.5;
		webgl.loadVertices(vertices, brillance, rx, cz);
	};

	const runshapes = (start, t) => {
		let comp = 0;
		let minComp = minComplexity;
		do {
			comp = 0;
			stack.length = 0;
			vertices.length = 0;
			brillance.length = 0;
			rule[start]([1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0], t);
			do {
				const s = stack.shift();
				if (s !== undefined && s[18] <= maxDepth) {
					shapes[s[19]](s);
					comp++;
				}
			} while (stack.length);
		} while (comp < minComp--);
	};

	const singlerule = (i) => {
		return (s, t) => {
			s = transform(s, t);
			if (s[17] === -1) return;
			s[19] = i;
			stack.push(s);
		};
	};

	const randomrule = (totalWeight, weight, index, len) => {
		return (s, t) => {
			s = transform(s, t);
			if (s[17] === -1) return;
			let w = 0;
			const r = random() * totalWeight;
			for (let i = 0; i < len; i++) {
				w += weight[i];
				if (r <= w) {
					s[19] = index[i];
					stack.push(s);
					return;
				}
			}
		};
	};

	// ---- init ----
	window.rule = {};
	window.CUBE = cube;
	window.SIZE = size;
	if (setup.seed) seed = setup.seed;
	else seed = Math.round(Math.random() * 1000);
	const minSize = setup.minSize || 0;
	const maxDepth = minSize === 0 ? setup.maxDepth || 100 : 1000000;
	const minComplexity = setup.minComplexity || 0;
	shapes.length = 0;
	for (const namerule in rules) {
		const r = rules[namerule];
		if (Array.isArray(r)) {
			let totalWeight = 0;
			const weight = [];
			const index = [];
			for (let i = 0; i < r.length; i += 2) {
				totalWeight += r[i];
				shapes.push(r[i + 1]);
				weight.push(r[i]);
				index.push(shapes.length - 1);
			}
			rule[namerule] = randomrule(totalWeight, weight, index, index.length);
		} else {
			shapes.push(r);
			rule[namerule] = singlerule(shapes.length - 1);
		}
	}

	newStructure();
	webgl.resize();
	webgl.anim();
	
	window.addEventListener("pointerdown", (e) => newStructure(), false);

};

////////////////////////////////////////////////////////
/**

 Structure adapted from 
 https://github.com/ankurpawar/StructureSynth/blob/master/FrameFractal/framefractal1.es
 http://structuresynth.sourceforge.net/index.php

 */

const setup = {
	start: "start",
	transform: { s: 0.1, b: 0.5},
	minSize: 0.00001,
	minComplexity: 5000,
	seed: 1741865996,
	fov: 60,
	camDist: 1,
	lightPosition: [0, 0.9, -0.5],
	ambientColor: [0.0, 0.0, 0.0],
	specularColor: [0.2, 0.4, 0.6],
	diffuseColor: [0.7, 0.35, 0]
};

const rules = {
	start: s => {
		rule.design(s, {rz: 90});
		rule.design(s, {rz: -90});
		rule.design(s, {ry: 0});
		rule.design(s, {ry: 90});
		rule.design(s, {ry: 180});
		rule.design(s, {ry: 270});
	},
	design: [
		1, s => {
			CUBE(s);
			CUBE(s, {s: [0.25, 1, 1], rx: 45})
			rule.design(s, {x: 0.92, s: 0.92});
		},
		0.025, s => {
				rule.design(s, {ry: 90});
				rule.design(s, {ry: -90});
				rule.design(s, {rz: 90});
				rule.design(s, {rz: -90});
				rule.design(s);
		}
	]
};

structure(setup, rules);